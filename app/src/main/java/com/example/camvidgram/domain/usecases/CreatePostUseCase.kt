package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: Post) {
        repository.savePost(post)
    }
}