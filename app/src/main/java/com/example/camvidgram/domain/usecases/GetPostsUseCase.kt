package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> {
        return repository.getAllPosts()
    }
}