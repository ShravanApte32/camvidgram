package com.example.camvidgram.core.di

import com.example.camvidgram.data.remote.api.PostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return object : PostApi {
            override suspend fun getPosts(): List<com.example.camvidgram.data.remote.dto.PostDto> {
                return emptyList()
            }

            override suspend fun likePost(postId: String) {
                // Fake implementation - does nothing
            }

            override suspend fun unlikePost(postId: String) {
                // Fake implementation - does nothing
            }

            override suspend fun addComment(postId: String, comment: String) {
                // Fake implementation - does nothing
            }
        }
    }
}