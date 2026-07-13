package com.project.routes

import com.project.data.model.requests.AddNewParticipantRequest
import com.project.data.model.responses.BaseResponse
import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.ShoppingParticipantUseCase
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

fun Route.shoppingParticipantRoute(shoppingParticipantUseCase: ShoppingParticipantUseCase, userUseCase: UserUseCase, categoryUseCase: CategoryUseCase) {
    authenticate("jwt") {
        post("api/v1/invite-participant"){
            val inviteRequest = call.receiveNullable<AddNewParticipantRequest>()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }
            try {
                val principal = call.principal<JWTPrincipal>()
                val phone = principal!!.payload.getClaim("phone").asString()
                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.GENERAL))
                    return@post
                }
                val senderUser = userUseCase.getUserByPhone(phone)
                val category = categoryUseCase.getCategoryById(inviteRequest.categoryId)
                if (category == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.CATEGORY_NOT_FOUND))
                    return@post
                }
                if(senderUser?.id != category.userId ){
                    call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.NO_ACCESS))
                    return@post
                }

                val recieverUser = userUseCase.getUserByPhone(inviteRequest.participantPhone)
                if (recieverUser == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@post
                }
                val isAdded = shoppingParticipantUseCase.addNewParticipant(category.categoryId?:0, recieverUser?.id ?: 0)

                if (isAdded) {
                    call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.PARTICIPANT_ADDED))
                    return@post
                } else {
                    call.respond(HttpStatusCode.InternalServerError, BaseResponse(false, Constants.Error.GENERAL))
                    return@post
                }

            }catch ( e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }
    }
}