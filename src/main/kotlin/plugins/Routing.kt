package com.project.plugins

import com.project.domain.usecase.CategoryUseCase
import com.project.domain.usecase.ProductUseCase
import com.project.domain.usecase.UserUseCase
import com.project.routes.categoryRoute
import com.project.routes.productRoute
import com.project.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(userUseCase: UserUseCase, productUseCase: ProductUseCase, categoryUseCase: CategoryUseCase) {
    routing {
       userRoute(userUseCase)
        categoryRoute(categoryUseCase, userUseCase)
        productRoute(productUseCase, categoryUseCase, userUseCase )

    }
}