package com.example.camvidgram.domain.models

data class Comment(
    val id: String,
    val postId: String,
    val userName: String,
    val text: String,
    val time: String,
    val profileImageUrl: String?
)

