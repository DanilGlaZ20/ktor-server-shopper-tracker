package com.project

import com.project.authentification.JwtService
import com.project.data.repository.CategoryRepositoryImpl
import com.project.data.repository.ProductRepositoryImpl
import com.project.data.repository.ShoppingParticipantRepositoryImpl
import com.project.data.repository.UserRepositoryIImpl
import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.ProductUseCase
import com.project.domain.usecase.ShoppingParticipantUseCase
import com.project.domain.usecase.UserUseCase
import com.project.plugins.DatabasesFactory.initializeDatabase
import com.project.plugins.configureRouting
import com.project.plugins.configureSecurity
import com.project.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val jwtService = JwtService()
    val userRepository = UserRepositoryIImpl()
    val categoryRepository = CategoryRepositoryImpl()
    val productRepository = ProductRepositoryImpl()
    val shoppingParticipantRepository = ShoppingParticipantRepositoryImpl()
    val userUseCase = UserUseCase(userRepository, jwtService)
    val categoryUseCase = CategoryUseCase(categoryRepository, categoryRepository)
    val productUseCase = ProductUseCase(productRepository)
    val shoppingParticipantUseCase = ShoppingParticipantUseCase(shoppingParticipantRepository, shoppingParticipantRepository)
    initializeDatabase()
    configureSerialization()
    configureSecurity(userUseCase)
    configureRouting(userUseCase, productUseCase, categoryUseCase,shoppingParticipantUseCase)
}
