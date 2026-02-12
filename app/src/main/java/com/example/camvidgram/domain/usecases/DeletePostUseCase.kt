package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend operator fun invoke(postId: String) {
        repository.deletePost(postId)
    }
}