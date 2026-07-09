package com.project.authentification

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.project.data.model.UserModel
import java.time.LocalDateTime
import java.time.ZoneOffset


class JwtService {
    private val issuer = "ktor-project-serve-shopper-tracker"
    private val jwtSecret = System.getenv("JWT_SECRET") ?: "super_secret_local_key_12345"
    private val algorithm = Algorithm.HMAC256(jwtSecret)

    private val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: UserModel): String {
        return JWT.create()
            .withSubject("ShopperTrackerAppAuthentification")
            .withIssuer(issuer)
            .withClaim("phone", user.phone)
            .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))
            .sign(algorithm)
    }

    fun getVerifier(): JWTVerifier = verifier
}