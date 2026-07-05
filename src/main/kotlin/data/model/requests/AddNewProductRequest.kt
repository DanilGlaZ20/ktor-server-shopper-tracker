package com.project.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddNewProductRequest(
    val productId: Int? = null,
    val categoryId: Int? = null,
    val productTitle: String,
    val productDescription: String? = null,
    val isBought: Boolean = false
)
