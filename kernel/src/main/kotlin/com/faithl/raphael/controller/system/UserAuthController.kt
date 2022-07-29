package com.faithl.raphael.controller.system

import com.faithl.raphael.service.UserService
import com.faithl.raphael.vo.RapResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/user/auth")
class UserAuthController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping("/login")
    fun login(email: String, password: String): RapResult {
        return userService.login(email, password)
    }

    @RequestMapping("/register")
    fun register(email: String, username: String, password: String): RapResult {
        return userService.register(email, username, password)
    }

    @RequestMapping("/check-login")
    fun checkLogin(): RapResult {
        val data = userService.checkLogin()
        return if (data) {
            RapResult(200, "success", userService.getUserInfo())
        } else {
            RapResult(401, "fail")
        }
    }

    @RequestMapping("/logout")
    fun logout(): RapResult {
        userService.logout()
        return RapResult(200, "success")
    }

}