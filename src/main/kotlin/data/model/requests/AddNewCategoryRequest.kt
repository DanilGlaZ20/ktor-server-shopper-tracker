package com.project.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddNewCategoryRequest(
    val categoryId: Int? = null,
    val userId: Int? = null,
    @SerialName("category_title")
    val categoryTitle: String,
    @SerialName("category_description")
    val categoryDescription: String? = null
)
