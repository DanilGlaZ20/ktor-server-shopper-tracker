package com.project.domain.usecase

import com.project.data.model.ProductModel
import com.project.domain.repository.ItemRepository

class ProductUseCase(
    private val  productRepository: ItemRepository<ProductModel>
) {
    suspend fun addProduct(product: ProductModel) = productRepository.addItem(product)

    suspend fun getAllProducts():List<ProductModel> = productRepository.getItems()

    suspend fun updateProduct(product: ProductModel, categoryId: Int) = productRepository.updateItem(product, categoryId)

    suspend fun removeProduct(productId: Int, categoryId: Int) = productRepository.removeItem(productId, categoryId)
}