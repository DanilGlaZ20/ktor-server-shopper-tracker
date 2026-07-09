package com.project.domain.usecase

import com.auth0.jwt.JWTVerifier
import com.project.authentification.JwtService
import com.project.data.model.UserModel
import com.project.domain.repository.UserRepository

class UserUseCase(
    private  val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    suspend fun registerUser(user: UserModel) = userRepository.registerUser(user)
    suspend fun getUserByPhone(phone: String) = userRepository.getUserByPhone(phone)

    suspend fun getUserByUsername(username: String) = userRepository.getUserByUsername(username)

    suspend fun getAllUser() = userRepository.getAllUser()

    fun getJwtVerifier(): JWTVerifier = jwtService.getVerifier()
    fun getnerateToken(userModel: UserModel) = jwtService.generateToken(userModel)

}