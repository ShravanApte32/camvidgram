package com.example.camvidgram.data.mapper

import com.example.camvidgram.core.utils.formatTime
import com.example.camvidgram.data.local.entities.CommentEntity
import com.example.camvidgram.domain.models.Comment

fun CommentEntity.toDomain() = Comment(
    id = id,
    postId = postId,
    userName = userName,
    text = text,
    time = formatTime(time),
    profileImageUrl = profileImageUrl
)

fun Comment.toEntity(): CommentEntity =
    CommentEntity(id, postId, userName, text, time = System.currentTimeMillis(), profileImageUrl)
