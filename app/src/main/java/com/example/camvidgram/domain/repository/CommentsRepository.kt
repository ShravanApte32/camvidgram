package com.example.camvidgram.domain.repository

import com.example.camvidgram.domain.models.Comment
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    fun observeComments(postId: String): Flow<List<Comment>>
    suspend fun addComment(postId: String, text: String)
}
