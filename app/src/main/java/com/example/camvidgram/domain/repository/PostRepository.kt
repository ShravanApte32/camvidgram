package com.example.camvidgram.domain.repository

import com.example.camvidgram.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    // Local operations
    suspend fun savePost(post: Post)
    suspend fun deletePost(postId: String)
    suspend fun getPost(postId: String): Post?
    fun getAllPosts(): Flow<List<Post>>
    fun getPostsByUser(userId: String): Flow<List<Post>>

    // Remote operations
    suspend fun fetchPostsFromRemote(): List<Post>
    suspend fun likePost(postId: String)
    suspend fun unlikePost(postId: String)
    suspend fun addComment(postId: String, comment: String)

    // Sync operations
    suspend fun syncPosts(): Boolean
}