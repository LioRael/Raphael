package com.faithl.raphael.entity

import com.faithl.raphael.dao.UserModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

class User(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<User>(Users) {

        fun findByName(username: String): User? {
            return transaction { User.find { Users.username eq username }.firstOrNull() }
        }

        fun findByEmail(email: String): User? {
            return transaction { User.find { Users.email eq email }.firstOrNull() }
        }

    }

    var username by Users.username
    var password by Users.password
    var email by Users.email
    var isBanned by Users.isBanned
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt

    fun toUserModel(): UserModel {
        return UserModel(id.value, username, email, isBanned, createdAt.toString(), updatedAt.toString())
    }

}

object Users : IntIdTable("faithl_user") {

    val username = varchar("username", 24).uniqueIndex()
    val password = varchar("password", 255)
    val email = varchar("email", 255).uniqueIndex()
    val isBanned = bool("is_banned")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}