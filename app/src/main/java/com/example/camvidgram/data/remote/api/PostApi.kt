package com.example.camvidgram.data.remote.api

import com.example.camvidgram.data.remote.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @POST("posts/{postId}/like")
    suspend fun likePost(@Path("postId") postId: String)

    @POST("posts/{postId}/unlike")
    suspend fun unlikePost(@Path("postId") postId: String)

    @POST("posts/{postId}/comments")
    suspend fun addComment(@Path("postId") postId: String, comment: String)
}