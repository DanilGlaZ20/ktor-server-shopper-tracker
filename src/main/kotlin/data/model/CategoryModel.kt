package com.project.data.model

data class CategoryModel(
    val categoryId: Int,
    val userId: Int,
    val categoryTitle: String,
    val categoryDescription: String? = null,
)
