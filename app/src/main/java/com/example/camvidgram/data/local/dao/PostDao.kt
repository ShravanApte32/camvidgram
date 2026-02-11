package com.example.camvidgram.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.camvidgram.data.local.entities.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPostsByUser(userId: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: String): PostEntity?

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: String)

    @Query("UPDATE posts SET likes = likes + 1, isLiked = 1 WHERE id = :postId")
    suspend fun likePost(postId: String)

    @Query("UPDATE posts SET likes = likes - 1, isLiked = 0 WHERE id = :postId")
    suspend fun unlikePost(postId: String)

    @Query("UPDATE posts SET comments = comments + 1 WHERE id = :postId")
    suspend fun addComment(postId: String)

    @Query("DELETE FROM posts")
    suspend fun clearAll()
}