package com.project.routes

import com.project.data.model.CategoryModel
import com.project.data.model.UserModel
import com.project.data.model.requests.AddNewCategoryRequest
import com.project.data.model.responses.BaseResponse
import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.UserUseCase
import com.project.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.categoryRoute(categoryUseCase: CategoryUseCase, userUseCase: UserUseCase) {
    authenticate("jwt") {
        post("api/v1/create-category") {

            val categoryRequest = call.receiveNullable<AddNewCategoryRequest>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()

                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@post
                }

                val user = userUseCase.getUserByPhone(phone)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@post
                }
                val category = CategoryModel(
                    categoryId = 0,
                    userId = user.id,
                    categoryTitle = categoryRequest.categoryTitle,
                    categoryDescription = categoryRequest.categoryDescription
                )
                categoryUseCase.addCategory(category)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ADDED_SUCCESSFULLY))
            }catch(e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
                return@post
            }
        }

        post("api/v1/edit-category") {
            val user = call.principal<UserModel>()
            val categoryRequest = call.receiveNullable<AddNewCategoryRequest>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val userId = user!!.id
                val category = CategoryModel(
                    categoryId = categoryRequest.categoryId,
                    userId = userId,
                    categoryRequest.categoryTitle,
                    categoryRequest.categoryDescription
                )
                categoryUseCase.updateCategory(category, userId)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.EDITED_SUCCESSFULLY))

            }catch(e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
            }
        }

        post {
            val user = call.principal<UserModel>()
            val categoryRequest = call.request.queryParameters[Constants.Value.ID]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }
            try {
                val userId = call.principal<UserModel>()!!.id
                categoryUseCase.removeCategory(categoryRequest, userId)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.REMOVED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
            }
        }
    }
}