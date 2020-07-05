package com.example.myqiitaclient

data class Item(
    val id: String?,
    val title: String?,
    val body: String?,
    val url: String?,
    val comments_count: Int?,
    val like_count: Int?,
    val created_at: String?,
    val user: User?
)