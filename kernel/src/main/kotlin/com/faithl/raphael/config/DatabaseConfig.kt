package com.faithl.raphael.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "database")
class DatabaseConfig {

    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
    lateinit var driverClassName: String

}