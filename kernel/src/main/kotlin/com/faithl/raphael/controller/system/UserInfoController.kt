package com.faithl.raphael.controller.system

import com.faithl.raphael.service.UserService
import com.faithl.raphael.vo.RapResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/user")
class UserInfoController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping("/info")
    fun info(): RapResult {
        return if (userService.checkLogin()) {
            RapResult(200, "查询成功", userService.getUserInfo())
        } else {
            RapResult(401, "查询失败")
        }
    }

}