package com.project.routes

import com.project.data.model.ProductModel
import com.project.data.model.requests.AddNewProductRequest
import com.project.data.model.responses.BaseResponse
import com.project.data.model.tables.ProductTable.productId
import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.ProductUseCase
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

fun Route.productRoute(productUseCase: ProductUseCase, categoryUseCase: CategoryUseCase, userUseCase: UserUseCase) {
    authenticate("jwt") {
        post("api/v1/create-product") {
            val productRequest = call.receiveNullable<AddNewProductRequest>()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try{
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()

                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@post
                }
                val category = categoryUseCase.getCategoryById(productRequest.categoryId?:0)
                if(category== null){
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.CATEGORY_NOT_FOUND))
                    return@post
                }
               val product = ProductModel(
                    productId = 0,
                    categoryId =category.categoryId ?:0,
                   productTitle = productRequest.productTitle,
                   productDescription = productRequest.productDescription,
                   isBought = false

                )
                productUseCase.addProduct(product)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ADDED_SUCCESSFULLY))


            }catch(e: Exception){
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
                return@post
            }
        }

        put("api/v1/edit-product") {
            val productRequest = call.receiveNullable<AddNewProductRequest>()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@put
            }

            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if(phone == null){
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                    return@put
                }
                val category = categoryUseCase.getCategoryById(productRequest.categoryId?:0)
                if(category == null){
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.CATEGORY_NOT_FOUND))
                    return@put
                }

                val product = ProductModel(
                    productId = productRequest.productId?:0,
                    categoryId =category.categoryId ?:0,
                    productTitle = productRequest.productTitle,
                    productDescription = productRequest.productDescription,
                    isBought = productRequest.isBought
                )
                productUseCase.updateProduct(product, category.categoryId?:0)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.EDITED_SUCCESSFULLY))

            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-all-products") {
            val categoryId = call.request.queryParameters[Constants.Value.ID]?.toIntOrNull()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if(phone == null){
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                    return@get
                }
                val user = userUseCase.getUserByPhone(phone?:"")
                if(user == null){
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@get
                }
                val category = categoryUseCase.getCategoryById(categoryId)
                if (category == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.CATEGORY_NOT_FOUND))
                    return@get
                }
                if (category.userId != user.id) {
                    call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.NO_ACCESS))
                    return@get
                }
                else call.respond(HttpStatusCode.OK, productUseCase.getAllProducts(categoryId))

            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }


        }

        delete("api/v1/delete-product") {
            val productRequest = call.request.queryParameters[Constants.Value.ID]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }
            val categoryId = call.request.queryParameters["category_id"]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if(phone == null){
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                    return@delete
                }
                productUseCase.removeProduct(productRequest, categoryId)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.REMOVED_SUCCESSFULLY))
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }
    }

}