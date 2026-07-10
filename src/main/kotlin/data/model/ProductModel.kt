package com.project.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductModel(
    @SerialName("product_id")
    val productId: Int,

    @SerialName("category_id")
    val categoryId: Int?,

    @SerialName("product_name")
    val productTitle: String,

    @SerialName("product_description")
    val productDescription: String? = null,

    @SerialName("is_buy")
    val isBought: Boolean = false ,
)
