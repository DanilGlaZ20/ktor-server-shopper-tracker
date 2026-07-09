package com.project.data.repository

import com.project.data.model.ProductModel
import com.project.data.model.tables.CategoryTable
import com.project.data.model.tables.ProductTable
import com.project.data.model.tables.UserTable
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

class ProductRepositoryImpl: ItemRepository<ProductModel>, Modifier<ProductModel> {
    override suspend fun addItem(item: ProductModel) {
        dbQuery {
            ProductTable.insert { table->
                table[ProductTable.productId] = item.productId
                table[ProductTable.productTitle] = item.productTitle
                table[ProductTable.productDescription] = item.productDescription
            }
        }
    }

    override suspend fun getItems(): List<ProductModel> {
       return dbQuery { ProductTable.selectAll().mapNotNull { row -> rowToItem(row) } }
    }

    override suspend fun updateItem(item: ProductModel, otherId: Int) {
       dbQuery { ProductTable.update({
           (ProductTable.productId eq item.productId)and (ProductTable.categoryId eq otherId)
       }) { table ->
           table[ProductTable.productId] = item.productId
           table[ProductTable.productTitle] = item.productTitle

       } }
    }

    override suspend fun removeItem(itemId: Int, otherId: Int) {
        dbQuery { ProductTable.deleteWhere { (ProductTable.productId eq itemId) and (ProductTable.productId eq otherId) } }
    }
    override fun rowToItem(row: ResultRow?): ProductModel? {
        if (row == null) return null
        return ProductModel(
            row[ProductTable.productId],
            row[ProductTable.categoryId],
            row[ProductTable.productTitle],
            row[ProductTable.productDescription]
        )
    }


}