package com.faithl.raphael.config

import cn.dev33.satoken.stp.StpInterface
import com.faithl.raphael.entity.Permission
import org.springframework.stereotype.Component


@Component
class StpInterfaceImpl: StpInterface {

    override fun getPermissionList(loginId: Any, loginType: String): List<String> {
        return Permission.findPermissions(loginId.toString().toInt())
    }

    override fun getRoleList(loginId: Any, loginType: String): List<String> {
        return listOf()
    }

}