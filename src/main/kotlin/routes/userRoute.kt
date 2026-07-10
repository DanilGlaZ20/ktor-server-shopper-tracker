package com.project.routes

import com.project.authentification.hash
import com.project.data.model.UserModel
import com.project.data.model.requests.LoginRequest
import com.project.data.model.requests.RegisterRequest
import com.project.data.model.responses.BaseResponse
import com.project.domain.usecase.UserUseCase
import com.project.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.userRoute(userUseCase: UserUseCase) {
    val hashFunction = {p: String -> hash(password = p) }

    post("/api/v1/signup"){
        val registerRequest = call.receiveNullable<RegisterRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }
        try {
            val hashedPassword = hashFunction(registerRequest.password)
            val user = UserModel(
                id = 0,
                username = registerRequest.username,
                password = hashedPassword,
                phone = registerRequest.phone,
            )
            userUseCase.registerUser(user)
            val token = userUseCase.getnerateToken(user)
            call.respond(HttpStatusCode.OK, BaseResponse(true, token))

        }catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL ))
        }
    }

    post("api/v1/login") {
        val loginRequest = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }
        try{
            val user = userUseCase.getUserByUsername(loginRequest.username.trim())
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.WRONG_USERNAME_OR_PASSWORD))
                return@post
            }
            else{
                if(user.password == hashFunction(loginRequest.password)){
                    call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.getnerateToken(user)))
                    return@post
                } else {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.WRONG_USERNAME_OR_PASSWORD))
                    return@post
                }
            }
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.GENERAL))
        }
    }

    authenticate("jwt"){
        get("/api/v1/get-user-info"){
            try{
                val principal = call.principal<JWTPrincipal>()
                val phone = principal?.payload?.getClaim("phone")?.asString()
                if (phone == null) {
                    call.respond(HttpStatusCode.Unauthorized, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                    return@get
                }
                val user = userUseCase.getUserByPhone(phone)
                if (user != null){
                    val safeUser = user.copy(password = "")
                    call.respond(HttpStatusCode.OK, safeUser)
                } else call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
            }
            catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.GENERAL))
            }
        }

    }
}