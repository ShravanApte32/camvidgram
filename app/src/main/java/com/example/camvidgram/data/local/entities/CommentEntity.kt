package com.example.camvidgram.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "post_id")
    val postId: String,

    @ColumnInfo(name = "user_name")
    val userName: String,

    val text: String,
    val time: Long,

    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String?
)

