package com.project.utils

import com.project.data.model.tables.CategoryTable
import org.jetbrains.exposed.sql.ResultRow
interface Modifier<T>{
    fun  rowToItem (row: ResultRow?): T?
}
