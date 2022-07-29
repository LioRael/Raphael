package com.faithl.raphael.dao

data class RepoModel(
    val id: String,
    val name: String,
    val description: String,
    val visible: String,
    val workspace: String,
    val readable: Boolean,
    val writable: Boolean,
)