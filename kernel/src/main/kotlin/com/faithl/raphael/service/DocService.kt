package com.faithl.raphael.service

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.lang.UUID
import cn.hutool.core.lang.tree.Tree
import cn.hutool.core.lang.tree.TreeNode
import cn.hutool.core.lang.tree.TreeNodeConfig
import cn.hutool.core.lang.tree.TreeUtil
import com.faithl.raphael.entity.Document
import com.faithl.raphael.entity.Documents
import com.faithl.raphael.entity.Repository
import com.faithl.raphael.vo.RapResult
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class DocService {

    fun query(): List<String> {
        return listOf("doc1", "doc2", "doc3")
    }

    fun list(repoId: String): RapResult {
        val repo = transaction { Repository.findById(repoId) }
        return if (repo == null) {
            RapResult(404, "repo not found")
        } else {
            if (repo.visible != "public") {
                if (!StpUtil.isLogin()) {
                    return RapResult(401, "请先登录")
                }else if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-read")) {
                    val documents = transaction { Document.find { Documents.repo eq repoId }.toList() }
                    val result = sort(documents)
                    RapResult(200, "success", result)
                } else {
                    RapResult(403, "forbidden")
                }
            } else {
                val documents = transaction { Document.find { Documents.repo eq repoId }.toList() }
                val result = sort(documents)
                RapResult(200, "success", result)
            }
        }
    }

    fun sort(documents: List<Document>): MutableList<Tree<String>> {
        val nodeList = CollUtil.newArrayList<TreeNode<String>>()
        if (documents.isEmpty()) {
            return CollUtil.newArrayList()
        }
        documents.forEach {
            transaction { nodeList.add(TreeNode(it.id.value, it.parent?.id?.value, it.title, it.index)) }
        }
        val config = TreeNodeConfig()
        config.nameKey = "title"
        config.idKey = "key"
        val treeNodes: MutableList<Tree<String>> = TreeUtil.build(
            nodeList, null, config
        ) { treeNode: TreeNode<String>, tree: Tree<String> ->
            val document = documents.find { treeNode.id == it.id.value }
            tree.id = treeNode.id
            tree.parentId = treeNode.parentId
            tree.weight = treeNode.weight
            tree.name = treeNode.name
            tree.putExtra("content", document?.content)
            tree.putExtra("type", document?.type)
        }
        return treeNodes
    }

    fun detail(repoId: String, documentId: String): RapResult {
        val repo = transaction { Repository.findById(repoId) }
        return if (repo == null) {
            RapResult(404, "repo not found")
        } else {
            if (repo.visible != "public") {
                if (!StpUtil.isLogin()) {
                    return RapResult(401, "请先登录")
                }else if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-read")) {
                    val document = transaction {
                        Document.find { Documents.repo eq repoId and (Documents.id eq documentId) }.firstOrNull()
                    }
                    if (document == null) {
                        RapResult(404, "document not found")
                    } else {
                        RapResult(200, "success", document.toDocModel())
                    }
                } else {
                    RapResult(403, "forbidden")
                }
            } else {
                val document = transaction {
                    Document.find { Documents.repo eq repoId and (Documents.id eq documentId) }.firstOrNull()
                }
                if (document == null) {
                    RapResult(404, "document not found")
                } else {
                    RapResult(200, "success", document.toDocModel())
                }
            }
        }
    }

    fun create(
        repoId: String,
        parentId: String?,
        type: String,
        title: String,
        content: String?
    ): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        val repo = transaction { Repository.findById(repoId) }
        val parent = parentId?.let {
            transaction {
                Document.find { Documents.repo eq repoId and (Documents.id eq parentId) }.firstOrNull()
            }
        }
        val index = transaction {
            val docs = Document.find { Documents.repo eq repoId and (Documents.parent eq parentId) }
            if (docs.empty()) {
                0
            } else {
                docs.sortedBy { it.index }.last().index + 1
            }
        }
        return if (repo == null) {
            RapResult(404, "repo not found")
        } else {
            val id = UUID.randomUUID().toString(true).slice(1..6)
            if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-edit")) {
                val document = transaction {
                    Document.new(id) {
                        this.repo = repo
                        this.parent = parent
                        this.index = index
                        this.type = type
                        this.title = title
                        this.content = content
                        this.createdAt = LocalDateTime.now()
                        this.updatedAt = LocalDateTime.now()
                    }
                }
                RapResult(200, "success", document.toDocModel())
            } else {
                RapResult(403, "forbidden")
            }
        }
    }

    fun update(repoId: String, docId: String, title: String, content: String?): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        val repo = transaction { Repository.findById(repoId) }
        val document = transaction {
            Document.find { Documents.repo eq repoId and (Documents.id eq docId) }.firstOrNull()
        }
        return if (repo == null) {
            RapResult(404, "repo not found")
        } else if (document == null) {
            RapResult(404, "doc not found")
        } else {
            if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-edit")) {
                transaction {
                    document.apply {
                        this.title = title
                        this.content = content
                        this.updatedAt = LocalDateTime.now()
                    }
                }
                RapResult(200, "success", document.toDocModel())
            } else {
                RapResult(403, "forbidden")
            }
        }
    }

    fun delete(repoId: String, docId: String): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        val repo = transaction { Repository.findById(repoId) } ?: return RapResult(404, "repo not found")
        val doc = transaction { Document.find { Documents.repo eq repoId and (Documents.id eq docId) }.firstOrNull() }
        return if (doc === null) {
            RapResult(404, "doc not found")
        } else {
            if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-edit")) {
                transaction {
                    doc.delete()
                }
                RapResult(200, "success")
            } else {
                RapResult(403, "forbidden")
            }
        }
    }

    fun move(repoId: String, docId: String, parentId: String?, index: Int): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        val repo = transaction { Repository.findById(repoId) } ?: return RapResult(404, "repo not found")
        val doc = transaction { Document.find { Documents.repo eq repoId and (Documents.id eq docId) }.firstOrNull() }
        return if (doc === null) {
            RapResult(404, "doc not found")
        } else {
            transaction {
                if (StpUtil.hasPermission(repo.workspace + "-" + repo.name + "-edit")) {
                    doc.parent = parentId?.let {
                        Document.find { Documents.repo eq repoId and (Documents.id eq it) }.firstOrNull()
                    }
                    doc.updatedAt = LocalDateTime.now()
                    RapResult(200, "success")
                } else {
                    RapResult(403, "forbidden")
                }
            }
        }
    }

}