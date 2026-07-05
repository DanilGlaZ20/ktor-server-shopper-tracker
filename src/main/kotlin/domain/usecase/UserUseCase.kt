package com.project.domain.usecase

import com.project.domain.repository.UserRepository

class UserUseCase(
    private  val userRepository: UserRepository
) {
    suspend fun getUserByPhone(phone: String) = userRepository.getUserByPhone(phone)

    suspend fun getUserByUsername(username: String) = userRepository.getUserByUsername(username)

    suspend fun getAllUser() = userRepository.getAllUser()

}