package com.example.camvidgram.data.repository

import com.example.camvidgram.data.local.dao.CommentDao
import com.example.camvidgram.data.local.entities.CommentEntity
import com.example.camvidgram.data.mapper.toDomain
import com.example.camvidgram.domain.models.Comment
import com.example.camvidgram.domain.repository.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentDao: CommentDao
) : CommentsRepository {

    override fun observeComments(postId: String): Flow<List<Comment>> =
        commentDao.observeComments(postId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun addComment(postId: String, text: String) {
        val comment = CommentEntity(
            id = UUID.randomUUID().toString(),
            postId = postId,
            userName = "Test",                 // replace with real user
            text = text,
            time = System.currentTimeMillis(),
            profileImageUrl = null
        )
        commentDao.upsert(comment)
    }
}
