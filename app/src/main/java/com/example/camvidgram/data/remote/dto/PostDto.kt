package com.example.camvidgram.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("userProfileImage")
    val userProfileImage: String?,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("caption")
    val caption: String,

    @SerializedName("likes")
    val likes: Int,

    @SerializedName("comments")
    val comments: Int,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("isLiked")
    val isLiked: Boolean,

    @SerializedName("location")
    val location: String?,

    @SerializedName("aspectRatio")
    val aspectRatio: Float
)
