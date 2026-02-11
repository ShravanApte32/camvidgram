package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.repository.PostRepository
import javax.inject.Inject

class LikePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String) {
        repository.likePost(postId)
    }
}