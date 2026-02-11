package com.example.camvidgram.presentation.homepage.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camvidgram.domain.models.FeedUiState
import com.example.camvidgram.domain.usecases.GetPostsUseCase
import com.example.camvidgram.domain.usecases.LikePostUseCase
import com.example.camvidgram.domain.usecases.SyncPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val syncPostsUseCase: SyncPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<FeedEvent?>(null)
    val events = _events.asStateFlow()

    init {
        loadPosts()
        syncPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            getPostsUseCase()
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null
                    )
                }
                .catch { error ->
                    Timber.e(error, "Error loading posts")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load posts"
                    )
                }
                .collect { posts ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        posts = posts
                    )
                }
        }
    }

    fun refreshPosts() {
        loadPosts()
        syncPosts()
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            try {
                likePostUseCase(postId)
                // Update local state immediately for better UX
                val currentPosts = _uiState.value.posts.toMutableList()
                val postIndex = currentPosts.indexOfFirst { it.id == postId }

                if (postIndex != -1) {
                    val post = currentPosts[postIndex]
                    val updatedPost = if (post.isLiked) {
                        post.copy(
                            likes = post.likes - 1,
                            isLiked = false
                        )
                    } else {
                        post.copy(
                            likes = post.likes + 1,
                            isLiked = true
                        )
                    }

                    currentPosts[postIndex] = updatedPost
                    _uiState.value = _uiState.value.copy(
                        posts = currentPosts
                    )

                    // Show feedback
                    _events.value = if (updatedPost.isLiked) {
                        FeedEvent.PostLiked("Post liked!")
                    } else {
                        FeedEvent.PostUnliked("Post unliked!")
                    }
                    // Clear event after consumption
                    _events.value = null
                }
            } catch (e: Exception) {
                Timber.e(e, "Error liking post")
                _events.value = FeedEvent.Error("Failed to like post")
                _events.value = null
            }
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch {
            try {
                // For now, we'll use the same likePostUseCase (toggle behavior)
                // In a real app, you might have separate use cases
                likePostUseCase(postId)
            } catch (e: Exception) {
                Timber.e(e, "Error unliking post")
                _events.value = FeedEvent.Error("Failed to unlike post")
                _events.value = null
            }
        }
    }

    private fun syncPosts() {
        viewModelScope.launch {
            try {
                val success = syncPostsUseCase()
                if (success) {
                    Timber.d("Posts synced successfully")
                    _events.value = FeedEvent.SyncCompleted
                    _events.value = null
                } else {
                    _events.value = FeedEvent.Error("Sync failed")
                    _events.value = null
                }
            } catch (e: Exception) {
                Timber.e(e, "Error syncing posts")
                // Don't show error to user for background sync
            }
        }
    }

    fun retry() {
        loadPosts()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Handle configuration changes
    override fun onCleared() {
        super.onCleared()
        Timber.d("FeedViewModel cleared")
    }
}

// Events for one-time UI actions
sealed class FeedEvent {
    data class PostLiked(val message: String) : FeedEvent()
    data class PostUnliked(val message: String) : FeedEvent()
    data class Error(val message: String) : FeedEvent()
    object SyncCompleted : FeedEvent()
}