package com.faithl.raphael.entity

import com.faithl.raphael.dao.DocModel
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

class Document(id: EntityID<String>) : Entity<String>(id) {

    companion object : EntityClass<String, Document>(Documents)

    var repo by Repository referencedOn Documents.repo
    var parent by Document optionalReferencedOn Documents.parent
    var index by Documents.index
    var type by Documents.type
    var title by Documents.title
    var content by Documents.content
    var createdAt by Documents.createdAt
    var updatedAt by Documents.updatedAt

    fun toDocModel(): DocModel {
        return transaction {
            DocModel(
                repo.id.value,
                index,
                parent?.id?.value,
                type,
                title,
                content,
                createdAt,
                updatedAt
            )
        }
    }

}

object Documents : IdTable<String>("raphael_documents") {

    override val id: Column<EntityID<String>> = varchar("id", 32).entityId()
    final override val primaryKey = PrimaryKey(id)
    val repo = reference("repo", Repositories)
    val parent = reference("parent", Documents).nullable()
    val index = integer("index").default(0)
    val type = varchar("type", 32).default("text")
    val title = varchar("title", 255)
    val content = text("content").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

}