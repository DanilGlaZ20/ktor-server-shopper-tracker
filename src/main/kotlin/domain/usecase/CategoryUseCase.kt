package com.project.domain.usecase

import com.project.data.model.CategoryModel
import com.project.domain.repository.ItemRepository
import com.project.utils.CategoryFinder

class CategoryUseCase(
   private  val categoryRepository: ItemRepository<CategoryModel>,
   private val categoryFinder: CategoryFinder
) {
     suspend fun addCategory(category: CategoryModel) = categoryRepository.addItem(category)

    suspend fun getAllCategories(userId: Int): List<CategoryModel> =  categoryRepository.getItems(userId)

    suspend fun updateCategory(category: CategoryModel, userId: Int) = categoryRepository.updateItem(category, userId)

    suspend fun removeCategory(categoryId: Int, userId: Int ) = categoryRepository.removeItem(categoryId, userId)

    suspend fun getCategoryById(categoryId: Int) = categoryFinder.getCategoryById(categoryId)

}