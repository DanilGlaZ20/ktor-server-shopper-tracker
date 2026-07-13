package com.project.data.repository

import com.project.data.model.CategoryModel
import com.project.data.model.ShoppingParticipantModel
import com.project.data.model.tables.CategoryTable
import com.project.data.model.tables.ShoppingParticipantTable
import com.project.domain.repository.ShoppingParticipantRepository
import com.project.plugins.DatabasesFactory.dbQuery
import com.project.utils.IdFinder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import kotlin.math.absoluteValue

class ShoppingParticipantRepositoryImpl: ShoppingParticipantRepository, IdFinder<List<Int>> {
    override suspend fun inviteShoppingParticipant(categoryId: Int, participantPhone: Int): Boolean {
        return dbQuery {
            ShoppingParticipantTable.insert { table->
               table[ShoppingParticipantTable.shoppingId] = categoryId
               table[ShoppingParticipantTable.userId] = participantPhone
           }.insertedCount > 0

        }
    }

    override suspend fun removeParticipant(categoryId: Int, participantPhone: Int){
        return dbQuery { ShoppingParticipantTable.deleteWhere {
            (ShoppingParticipantTable.shoppingId.eq(categoryId)) and (ShoppingParticipantTable.userId.eq(participantPhone)) }
        }
    }

    override suspend fun getItemById(id: Int): List<Int> {
        return dbQuery {
            ShoppingParticipantTable.select({ ShoppingParticipantTable.shoppingId eq id }).map { it[ShoppingParticipantTable.userId] }
        }
    }



}