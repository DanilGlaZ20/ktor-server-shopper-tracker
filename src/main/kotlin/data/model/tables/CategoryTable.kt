package com.project.data.model.tables

import org.jetbrains.exposed.sql.Table

object CategoryTable: Table("categories") {
    val categoryId = integer("category_id").autoIncrement()
    val userId = integer("user_id")
    val categoryTitle = varchar("category_title", 255)
    val categoryDescription = varchar("category_description", 255).nullable()

    override val primaryKey = PrimaryKey(categoryId)
}