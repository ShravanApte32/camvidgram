package com.example.camvidgram.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    indices = [Index(value = ["postId"])]
)
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val userName: String,
    val text: String,
    val time: Long,
    val profileImageUrl: String?
)
