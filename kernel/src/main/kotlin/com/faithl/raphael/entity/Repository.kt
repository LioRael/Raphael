package com.faithl.raphael.entity

import cn.dev33.satoken.stp.StpUtil
import com.faithl.raphael.dao.RepoModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

class Repository(id: EntityID<String>) : Entity<String>(id) {

    companion object : EntityClass<String, Repository>(Repositories) {

        fun findByName(name: String): Repository? {
            return transaction { Repository.find { Repositories.name eq name }.firstOrNull() }
        }

    }

    var name by Repositories.name
    var description by Repositories.description
    var visible by Repositories.visible
    var workspace by Repositories.workspace
    var createdAt by Repositories.createdAt
    var updatedAt by Repositories.updatedAt

    fun toRepoModel(): RepoModel {
        val objectMapper = ObjectMapper()
        return RepoModel(
            id.value,
            name,
            description,
            visible,
            workspace,
            StpUtil.hasPermission("$workspace-$name-read"),
            StpUtil.hasPermission("$workspace-$name-edit")
        )
    }

}

object Repositories : IdTable<String>("raphael_repositories") {

    override val id: Column<EntityID<String>> = varchar("id", 32).entityId()
    final override val primaryKey = PrimaryKey(id)
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val visible = varchar("visible", 16)
    val workspace = varchar("workspace", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}