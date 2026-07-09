package com.project.domain.repository

import com.project.data.model.UserModel


interface UserRepository {
    suspend fun registerUser(user: UserModel)
    suspend fun getUserByPhone(phone: String): UserModel?
    suspend fun getAllUser(): List<UserModel>
    suspend fun getUserByUsername(username: String): UserModel?
}