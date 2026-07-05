package com.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int,
    val username: String,
    val password: String,
    val phone: String,

)
