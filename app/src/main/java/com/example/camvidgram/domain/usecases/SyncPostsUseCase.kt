package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.repository.PostRepository
import javax.inject.Inject

class SyncPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.syncPosts()
    }
}