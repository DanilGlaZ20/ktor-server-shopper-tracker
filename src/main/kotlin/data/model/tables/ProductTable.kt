package com.project.data.model.tables

import org.jetbrains.exposed.sql.Table

object ProductTable: Table("Products") {
    val productId = integer("product_id").autoIncrement()
    val categoryId = integer("category_id")
    val productTitle = varchar("product_name", 255)
    val productDescription = varchar("product_description", 510).nullable()
    val isBuy = bool("is_buy")

    override  val primaryKey =PrimaryKey(productId)
}