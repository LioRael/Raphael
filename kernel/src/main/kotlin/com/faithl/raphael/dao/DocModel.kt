package com.faithl.raphael.dao

import java.time.LocalDateTime

data class DocModel(
    val repo: String,
    val index: Int,
    val parent: String?,
    val type: String,
    val title: String,
    val content: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)