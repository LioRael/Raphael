package com.faithl.raphael.service

import cn.dev33.satoken.stp.StpUtil
import com.faithl.raphael.dao.UserModel
import com.faithl.raphael.entity.Permission
import com.faithl.raphael.entity.User
import com.faithl.raphael.vo.RapResult
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import java.time.LocalDateTime

@Service
class UserService {

    fun login(email: String, password: String): RapResult {
        if (email.isEmpty() || password.isEmpty()) {
            return RapResult(400, "邮箱或密码不能为空")
        } else if (!email.matches(Regex("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"))) {
            return RapResult(400, "邮箱不合法")
        } else if (password.length < 8) {
            return RapResult(400, "密码不能少于8位")
        } else {
            val user = User.findByEmail(email)
            return if (user == null) {
                RapResult(400, "邮箱不存在")
            } else if (user.password != DigestUtils.md5DigestAsHex(password.toByteArray())) {
                RapResult(400, "密码错误")
            } else {
                StpUtil.login(user.id.value)
                RapResult(200, "恭喜你登录成功", mapOf("token" to StpUtil.getTokenValue()))
            }
        }
    }

    fun register(email: String, username: String, password: String): RapResult {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            return RapResult(400, "请检测你的参数是否为空")
        } else if (!email.matches(Regex("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"))) {
            return RapResult(400, "邮箱不合法")
        } else if (password.length < 8) {
            return RapResult(400, "密码不能少于8位")
        } else {
            val user = User.findByEmail(email)
            return if (user != null) {
                RapResult(400, "邮箱已存在")
            } else {
                val newUser = transaction {
                    User.new {
                        this.email = email
                        this.password = DigestUtils.md5DigestAsHex(password.toByteArray())
                        this.username = username
                        this.createdAt = LocalDateTime.now()
                        this.updatedAt = LocalDateTime.now()
                        this.isBanned = false
                    }
                }
                StpUtil.login(newUser.id.value)
                Permission.addPermission("$username*")
                RapResult(200, "恭喜你注册成功", mapOf("token" to StpUtil.getTokenValue()))
            }
        }
    }

    fun checkLogin(): Boolean {
        return StpUtil.isLogin()
    }

    fun logout() {
        StpUtil.logout()
    }

    fun getUserInfo(): UserModel {
        return transaction { User.findById(StpUtil.getLoginIdAsInt())!!.toUserModel() }
    }

}