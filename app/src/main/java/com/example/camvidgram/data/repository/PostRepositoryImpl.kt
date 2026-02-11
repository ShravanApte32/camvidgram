package com.example.camvidgram.data.repository

import com.example.camvidgram.data.local.dao.PostDao
import com.example.camvidgram.data.local.entities.toEntity
import com.example.camvidgram.data.remote.api.PostApi
import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val postApi: PostApi // We'll create this later
) : PostRepository {

    override suspend fun savePost(post: Post) {
        postDao.insertPost(post.toEntity())
    }

    override suspend fun deletePost(postId: String) {
        postDao.deletePost(postId)
    }

    override suspend fun getPost(postId: String): Post? {
        return postDao.getPostById(postId)?.toDomain()
    }

    override fun getAllPosts(): Flow<List<Post>> {
        return postDao.getAllPosts()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override fun getPostsByUser(userId: String): Flow<List<Post>> {
        return postDao.getPostsByUser(userId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun fetchPostsFromRemote(): List<Post> {
        // TODO: Implement actual API call
        // For now, return dummy data
        return getDummyPosts()
    }

    override suspend fun likePost(postId: String) {
        // Update locally
        postDao.likePost(postId)

        // TODO: Sync with remote
        // postApi.likePost(postId)
    }

    override suspend fun unlikePost(postId: String) {
        // Update locally
        postDao.unlikePost(postId)

        // TODO: Sync with remote
        // postApi.unlikePost(postId)
    }

    override suspend fun addComment(postId: String, comment: String) {
        // Update locally
        postDao.addComment(postId)

        // TODO: Sync with remote
        // postApi.addComment(postId, comment)
    }

    override suspend fun syncPosts(): Boolean {
        return try {
            // Fetch from remote
            val remotePosts = fetchPostsFromRemote()

            // Convert to entities
            val entities = remotePosts.map { it.toEntity() }

            // Save to local database
            postDao.insertPosts(entities)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getDummyPosts(): List<Post> {
        return listOf(
            Post(
                userId = "user1",
                username = "android_dev",
                userProfileImage = "https://picsum.photos/200",
                imageUrl = "https://picsum.photos/400/600",
                caption = "Beautiful sunset at the beach! #nature #sunset",
                likes = 245,
                comments = 32,
                location = "Miami Beach, Florida",
                aspectRatio = 0.67f
            ),
            Post(
                userId = "user2",
                username = "kotlin_coder",
                userProfileImage = "https://picsum.photos/201",
                imageUrl = "https://picsum.photos/400/500",
                caption = "Just finished my new Android app! #kotlin #androiddev",
                likes = 189,
                comments = 45,
                isLiked = true,
                location = "San Francisco, CA",
                aspectRatio = 0.8f
            ),
            Post(
                userId = "user3",
                username = "travel_lover",
                userProfileImage = "https://picsum.photos/202",
                imageUrl = "https://picsum.photos/400/400",
                caption = "Exploring the mountains ⛰️ #travel #adventure",
                likes = 312,
                comments = 28,
                location = "Swiss Alps",
                aspectRatio = 1.0f
            )
        )
    }
}