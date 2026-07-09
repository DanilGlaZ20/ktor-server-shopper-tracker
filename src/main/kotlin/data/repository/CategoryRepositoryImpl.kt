package com.project.data.repository

import com.project.data.model.CategoryModel
import com.project.data.model.tables.CategoryTable
import com.project.data.model.tables.CategoryTable.categoryId
import com.project.domain.repository.ItemRepository
import com.project.plugins.DatabasesFactory.dbQuery
import com.project.utils.Modifier
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class CategoryRepositoryImpl: ItemRepository<CategoryModel>, Modifier<CategoryModel> {
    override suspend fun addItem(item: CategoryModel) {
        dbQuery {
                CategoryTable.insert { table ->
                    table[userId] = item.userId
                    table[categoryTitle] = item.categoryTitle
                    table[categoryDescription] = item.categoryDescription // если понадобиться добавить

                }
        }
    }

    override suspend fun getItems(): List<CategoryModel> {
       return  dbQuery { CategoryTable.selectAll().mapNotNull{rowToItem(it)} }
    }

    override suspend fun updateItem(item: CategoryModel, otherId: Int) {
        dbQuery {
            CategoryTable.update({
                (CategoryTable.categoryId eq categoryId) and (CategoryTable.userId eq otherId)
            }) { table ->
                table[userId] = item.userId
                table[categoryTitle] = item.categoryTitle
            }
        }
    }

    override suspend fun removeItem(itemId: Int, otherId: Int) {
        dbQuery {
            CategoryTable.deleteWhere { (CategoryTable.categoryId eq itemId) and (CategoryTable.userId eq otherId) }
        }
    }

    override fun rowToItem(
        row: ResultRow?
    ): CategoryModel? {
        if (row == null) return null
        return CategoryModel(
            row[CategoryTable.categoryId],
            row[CategoryTable.userId],
            row[CategoryTable.categoryTitle],
            row[CategoryTable.categoryDescription]
        )
    }

}