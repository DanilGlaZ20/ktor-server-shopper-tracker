package com.project.plugins

import io.ktor.server.application.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.project.domain.usecase.UserUseCase
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(userUseCase: UserUseCase) {
    authentication {
        jwt("jwt") {
            verifier( userUseCase.getJwtVerifier())
            realm = "Service server"
            validate { credential ->
                val phone = credential.payload.getClaim("phone").asString()
                if (phone != null) {
                    JWTPrincipal(credential.payload)
                } else  null

            }
        }
    }
}