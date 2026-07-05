package com.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductModel(
    val productId: Int,
    val categoryId: Int,
    val productTitle: String,
    val productDescription: String? = null,
    val isBought: Boolean = false ,
)
