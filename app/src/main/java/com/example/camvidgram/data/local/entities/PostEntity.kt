package com.example.camvidgram.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.camvidgram.domain.models.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
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
    val aspectRatio: Float = 1.0f
) {
    fun toDomain(): Post {
        return Post(
            id = id,
            userId = userId,
            username = username,
            userProfileImage = userProfileImage,
            imageUrl = imageUrl,
            caption = caption,
            likes = likes,
            comments = comments,
            timestamp = timestamp,
            isLiked = isLiked,
            location = location,
            aspectRatio = aspectRatio
        )
    }
}

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        userId = userId,
        username = username,
        userProfileImage = userProfileImage,
        imageUrl = imageUrl,
        caption = caption,
        likes = likes,
        comments = comments,
        timestamp = timestamp,
        isLiked = isLiked,
        location = location,
        aspectRatio = aspectRatio
    )
}