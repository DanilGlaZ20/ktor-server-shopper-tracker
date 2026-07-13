package com.project.utils


interface IdFinder<T> {
    suspend fun getItemById(id: Int): T?
}