package com.project.domain.repository

interface ItemRepository<T> {
   suspend fun addItem(item: T)

   suspend fun getItems(): List<T>

   suspend  fun updateItem(item: T, otherId: Int)

   suspend fun removeItem(itemId: Int, otherId: Int)
}