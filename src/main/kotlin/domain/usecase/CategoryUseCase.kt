package com.project.domain.usecase

import com.project.data.model.CategoryModel
import com.project.domain.repository.ItemRepository

class CategoryUseCase(
   private  val categoryRepository: ItemRepository<CategoryModel>
) {
     suspend fun addCategory(category: CategoryModel) = categoryRepository.addItem(category)

    suspend fun getAllCategories(): List<CategoryModel> =  categoryRepository.getItems()

    suspend fun updateCategory(category: CategoryModel, userId: Int) = categoryRepository.updateItem(category, userId)

    suspend fun removeCategory(categoryId: Int, userId: Int ) = categoryRepository.removeItem(categoryId, userId)



}