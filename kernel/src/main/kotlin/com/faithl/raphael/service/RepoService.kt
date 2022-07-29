package com.faithl.raphael.service

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.lang.ObjectId
import com.faithl.raphael.entity.Permission
import com.faithl.raphael.entity.Repositories
import com.faithl.raphael.entity.Repository
import com.faithl.raphael.vo.RapResult
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RepoService {

    @Autowired
    lateinit var userService: UserService

    fun query(): RapResult {
        return RapResult(200, "success", transaction { Repository.all().map { it.toRepoModel() } })
    }

    fun list(workspace: String): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        return if (StpUtil.hasPermission("$workspace-list")) {
            RapResult(200, "success", transaction {
                Repository.find { Repositories.workspace like workspace }.map { it.toRepoModel() }
            })
        } else {
            RapResult(403, "forbidden")
        }
    }

    fun detail(name: String, workspace: String): RapResult {
        val repo = transaction {
            Repository.find { (Repositories.name like name) and (Repositories.workspace like workspace) }
                .firstOrNull()
        }
        return if (repo != null) {
            if (repo.visible != "public") {
                if (!StpUtil.isLogin()) {
                    return RapResult(401, "请先登录")
                }
                if (StpUtil.hasPermission("$workspace-$name-detail")) {
                    RapResult(200, "success", repo.toRepoModel())
                } else {
                    RapResult(403, "forbidden")
                }
            } else {
                RapResult(200, "success", repo.toRepoModel())
            }
        } else {
            RapResult(404, "not found")
        }
    }

    fun create(
        name: String,
        description: String,
        visible: String,
        workspace: String
    ): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        // TODO 英文验证
        val repo = transaction {
            Repository.find { (Repositories.workspace like (workspace)) and (Repositories.name like name) }
                .firstOrNull()
        }
        return if (repo != null) {
            RapResult(400, "该仓库已存在")
        } else {
            val id = ObjectId.next().toString()
            return if (StpUtil.hasPermission("$workspace-create")) {
                val created = transaction {
                    Repository.new(id) {
                        this.name = name
                        this.description = description
                        if (visible == "公开") {
                            this.visible = "public"
                        } else {
                            this.visible = "private"
                        }
                        this.workspace = workspace
                        this.createdAt = LocalDateTime.now()
                        this.updatedAt = LocalDateTime.now()
                    }
                }
                Permission.addPermission(
                    "$workspace-$name*",
                )
                return RapResult(200, "success", created.toRepoModel())
            } else {
                RapResult(403, "forbidden")
            }
        }
    }

    fun delete(id: String): RapResult {
        if (!StpUtil.isLogin()) {
            return RapResult(401, "请先登录")
        }
        val repo = transaction { Repository.findById(id) }
        return if (repo == null) {
            RapResult(400, "该仓库不存在")
        } else {
            return if (StpUtil.hasPermission("${repo.workspace}-${repo.name}-own")) {
                transaction {
                    Permission.deletePermission("${repo.workspace}-${repo.name}-detail")
                    Permission.deletePermission("${repo.workspace}-${repo.name}-read")
                    Permission.deletePermission("${repo.workspace}-${repo.name}-edit")
                    Permission.deletePermission("${repo.workspace}-${repo.name}-own")
                    repo.delete()
                }
                RapResult(200, "success")
            } else {
                RapResult(403, "forbidden")
            }
        }
    }

}