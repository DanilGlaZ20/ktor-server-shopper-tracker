package com.project.utils

import com.project.data.model.CategoryModel

interface CategoryFinder {
    suspend fun getCategoryById(id: Int): CategoryModel?
}