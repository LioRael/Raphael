package com.faithl.raphael.dao

data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val isBanned: Boolean,
    val createdAt: String,
    val updatedAt: String
)