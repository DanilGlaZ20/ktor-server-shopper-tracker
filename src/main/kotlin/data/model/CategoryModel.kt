package com.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val categoryId: Int?,
    val userId: Int,
    val categoryTitle: String,
    val categoryDescription: String? = null,
)
