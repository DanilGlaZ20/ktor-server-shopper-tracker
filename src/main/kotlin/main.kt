package com.project

import com.project.authentification.JwtService
import com.project.data.repository.CategoryRepositoryImpl
import com.project.data.repository.ProductRepositoryImpl
import com.project.data.repository.UserRepositoryIImpl
import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.ProductUseCase
import com.project.domain.usecase.UserUseCase
import com.project.plugins.DatabasesFactory.initializeDatabase
import com.project.plugins.configureRouting
import com.project.plugins.configureSecurity
import com.project.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtService = JwtService()
    val userRepository = UserRepositoryIImpl()
    val categoryRepository = CategoryRepositoryImpl()
    val productRepository = ProductRepositoryImpl()
    val userUseCase = UserUseCase(userRepository, jwtService)
    val categoryUseCase = CategoryUseCase(categoryRepository)
    val productUseCase = ProductUseCase(productRepository)
    initializeDatabase()
    configureSerialization()
    configureSecurity(userUseCase)
    configureRouting(userUseCase, productUseCase, categoryUseCase)
}
