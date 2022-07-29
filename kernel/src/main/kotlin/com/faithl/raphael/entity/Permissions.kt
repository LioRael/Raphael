package com.faithl.raphael.entity

import cn.dev33.satoken.stp.StpUtil
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class Permission(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Permission>(Permissions) {

        fun hasPermission(userId: Int, permission: String): Boolean {
            return transaction {
                find { (Permissions.user eq userId) and (Permissions.permission eq permission) }.empty().not()
            }
        }

        fun findPermissions(userId: Int): List<String> {
            return transaction { find { Permissions.user eq userId }.map { it.permission } }
        }

        fun addPermission(vararg perms: String) {
            perms.forEach { perm ->
                transaction {
                    new {
                        this.user = User.findById(StpUtil.getLoginIdAsInt())!!
                        this.permission = perm
                    }
                }
            }
        }

        fun deletePermission(vararg perms: String) {
            perms.forEach { perm ->
                transaction {
                    Permissions.deleteWhere { (Permissions.user eq StpUtil.getLoginIdAsInt()) and (Permissions.permission eq perm) }
                }
            }
        }

    }

    var user by User referencedOn Permissions.user
    var permission by Permissions.permission

}

object Permissions : IntIdTable("raphael_permission") {

    val user = reference("user", Users)
    val permission = varchar("permission", 255)

}