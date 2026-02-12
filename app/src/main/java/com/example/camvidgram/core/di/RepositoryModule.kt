package com.example.camvidgram.core.di

import com.example.camvidgram.data.repository.CommentsRepositoryImpl
import com.example.camvidgram.data.repository.PostRepositoryImpl
import com.example.camvidgram.domain.repository.CommentsRepository
import com.example.camvidgram.domain.repository.PostRepository
import com.example.camvidgram.domain.repository.SearchRepository
import com.example.camvidgram.domain.usecases.LoadSearchPostUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    abstract fun bindCommentsRepository(
        impl: CommentsRepositoryImpl
    ): CommentsRepository

    @Binds
    abstract fun bindSearchRepository(
        fake: LoadSearchPostUseCase
    ): SearchRepository
}