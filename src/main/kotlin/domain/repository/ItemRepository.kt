package com.project.domain.repository

import com.project.data.model.tables.UserTable.id
import com.sun.beans.introspect.PropertyInfo
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import org.jetbrains.exposed.sql.ResultRow

interface ItemRepository<T> {
   suspend fun addItem(item: T)

   suspend fun getItems(otherId: Int): List<T>

   suspend  fun updateItem(item: T, otherId: Int)

   suspend fun removeItem(itemId: Int, otherId: Int)

}