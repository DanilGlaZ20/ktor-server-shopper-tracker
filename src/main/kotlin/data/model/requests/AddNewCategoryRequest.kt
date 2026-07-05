package com.project.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddNewCategoryRequest(
    val categoryId: Int? = null,
    val userId: Int? = null,
    val categoryTitle: String,
    val categoryDescription: String? = null
)
