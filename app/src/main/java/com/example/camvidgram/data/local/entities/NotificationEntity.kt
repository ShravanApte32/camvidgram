package com.example.camvidgram.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val userAvatarUrl: String?,
    val actionType: ActionType, // LIKE, COMMENT, FOLLOW, FOLLOW_REQUEST, MENTION, TAG
    val actionText: String,
    val postId: String? = null,
    val postThumbnailUrl: String? = null,
    val commentText: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isFollowBack: Boolean = false // For follow requests
)

enum class ActionType {
    LIKE,
    COMMENT,
    FOLLOW,
    FOLLOW_REQUEST,
    MENTION,
    TAG
}