package com.project.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val id: Int? = null,
    val username: String,
    val password: String,
    val phone: String,
)
