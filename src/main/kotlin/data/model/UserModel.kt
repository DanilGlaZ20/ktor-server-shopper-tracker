package com.project.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: Int,

    @SerialName("username")
    val username: String,

    @SerialName("password")
    val password: String,

    @SerialName("phone")
    val phone: String,

)
