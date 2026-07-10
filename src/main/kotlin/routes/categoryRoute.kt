package com.project.routes

import com.project.data.model.CategoryModel
import com.project.data.model.UserModel
import com.project.data.model.requests.AddNewCategoryRequest
import com.project.data.model.responses.BaseResponse
import com.project.data.model.tables.CategoryTable.userId
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
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

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

        put("api/v1/edit-category") {
            val categoryRequest = call.receiveNullable<AddNewCategoryRequest>()?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@put
            }

            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@put
                }
                val user = userUseCase.getUserByPhone(phone)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@put
                }

                val category = CategoryModel(
                    categoryId = categoryRequest.categoryId,
                    userId = user.id,
                    categoryTitle = categoryRequest.categoryTitle,
                    categoryDescription = categoryRequest.categoryDescription,
                )
                categoryUseCase.updateCategory(category, user.id)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.EDITED_SUCCESSFULLY))

            }catch(e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
            }
        }

        get("api/v1/get-all-categories") {
            val userId= call.request.queryParameters[Constants.Value.ID]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@get
                }
                val user = userUseCase.getUserByPhone(phone)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND ))
                    return@get
                }
                if (user.id != userId) {
                    call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.NO_ACCESS))
                    return@get
                }
                else call.respond(HttpStatusCode.OK, categoryUseCase.getAllCategories(userId))

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.USER_NOT_FOUND))

            }

        }

        delete("api/v1/delete-category") {
            val categoryId = call.request.queryParameters[Constants.Value.ID]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()

                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@delete
                }

                val user = userUseCase.getUserByPhone(phone)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@delete
                }
                categoryUseCase.removeCategory(categoryId, user.id)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.REMOVED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
            }
        }
    }
}