package com.example.camvidgram.domain.models

import java.util.UUID

data class Post(
    val id: String,
    val userId: String,
    val username: String,
    val userProfileImage: String? = null,
    val imageUrl: String,
    val caption: String = "",
    val likes: Int = 0,
    val comments: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isLiked: Boolean = false,
    val location: String? = null,
    val aspectRatio: Float = 1.0f // For maintaining image aspect ratio
) {
    val formattedTime: String
        get() = formatTimestamp(timestamp)

    private fun formatTimestamp(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "Just now"
        }
    }
}