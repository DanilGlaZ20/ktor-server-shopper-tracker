package com.project.data.model.tables

import org.jetbrains.exposed.sql.Table

object UserTable: Table("users") {
    val id = integer(name = "id").autoIncrement()
    val username = varchar("username", 64)
    val password = varchar("password", 64)
    val phone = varchar("phone", 64)

    override val primaryKey = PrimaryKey(id)
}