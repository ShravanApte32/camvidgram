package com.example.camvidgram.data.mapper

import com.example.camvidgram.data.local.entities.PostEntity
import com.example.camvidgram.domain.models.Post

fun PostEntity.toDomain(): Post {
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