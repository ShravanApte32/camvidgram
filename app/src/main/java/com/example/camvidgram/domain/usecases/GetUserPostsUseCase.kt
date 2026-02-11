package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(userId: String): Flow<List<Post>> {
        return repository.getPostsByUser(userId)
    }
}