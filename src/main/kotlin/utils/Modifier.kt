package com.project.utils

import org.jetbrains.exposed.sql.ResultRow
interface Modifier<T>{
    fun  rowToItem (row: ResultRow?): T?
}
