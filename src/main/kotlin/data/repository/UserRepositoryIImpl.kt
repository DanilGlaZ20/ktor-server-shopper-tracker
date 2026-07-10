package com.project.data.repository

import com.project.data.model.UserModel
import com.project.data.model.tables.UserTable
import com.project.data.model.tables.UserTable.id
import com.project.domain.repository.UserRepository
import com.project.plugins.DatabasesFactory.dbQuery
import com.project.utils.Modifier
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserRepositoryIImpl: UserRepository, Modifier<UserModel> {
    override suspend fun registerUser(user: UserModel) {
         return dbQuery {
            UserTable.insert { table ->
                table[username] = user.username
                table[password] = user.password
                table[phone] = user.phone
            }


        }
    }

    override suspend fun getUserByPhone(phone: String): UserModel? {
        return dbQuery {
            UserTable.select(UserTable.phone eq phone).map { rowToItem(row = it) }.singleOrNull()
        }
    }

    override suspend fun getAllUser(): List<UserModel> {
        return dbQuery { UserTable.selectAll().mapNotNull {row -> rowToItem(row) } }

    }

    override suspend fun getUserByUsername(username: String): UserModel? {
      return dbQuery {
           UserTable.select(UserTable.username eq username).map { rowToItem(it) }.singleOrNull()
       }
    }

    override fun rowToItem(row: ResultRow?): UserModel? {
        if (row == null) return null
        return UserModel(
            row[UserTable.id],
            row[UserTable.username],
            row[UserTable.password],
            row[UserTable.phone]

        )
    }

}