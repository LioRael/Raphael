package com.faithl.raphael.common

import com.faithl.raphael.config.DatabaseConfig
import com.faithl.raphael.entity.Documents
import com.faithl.raphael.entity.Permissions
import com.faithl.raphael.entity.Repositories
import com.faithl.raphael.entity.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DatabaseFactory {

    @Autowired
    lateinit var databaseConfig: DatabaseConfig

    @Bean
    fun connect() {
        val pool = hikari()
        Database.connect(pool)
        createScheme()
    }

    fun createScheme() {
        transaction {
            SchemaUtils.create(Users, Permissions)
            SchemaUtils.create(Repositories, Documents)
        }
    }

    fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = databaseConfig.driverClassName
            jdbcUrl = databaseConfig.url
            username = databaseConfig.username
            password = databaseConfig.password
            maximumPoolSize = 3
            validate()
        }
        return HikariDataSource(config)
    }

}