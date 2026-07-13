package com.project.data.model.tables

import org.jetbrains.exposed.sql.Table

object ShoppingParticipantTable: Table("shopping_participants"){
    val shoppingId = integer("shopping_id").references(CategoryTable.categoryId, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)
    val userId = integer("user_id").references(UserTable.id, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey = PrimaryKey(userId, shoppingId)
}