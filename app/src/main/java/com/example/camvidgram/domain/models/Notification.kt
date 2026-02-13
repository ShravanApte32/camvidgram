package com.example.camvidgram.domain.models

sealed class NotificationItemModel {
    data class Header(
        val title: String
    ) : NotificationItemModel()

    data class Notification(
        val id: String,
        val username: String,
        val actionText: String,
        val timeAgo: String,
        val avatarUrl: String? = null,
        val postThumbnailUrl: String? = null,
        val isFollowRequest: Boolean = false,
        val isUnread: Boolean = true
    ) : NotificationItemModel()
}