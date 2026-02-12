package com.example.camvidgram.presentation.homepage.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camvidgram.domain.models.Comment
import com.example.camvidgram.domain.repository.CommentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentsRepository
) : ViewModel() {

    fun getComments(postId: String): Flow<List<Comment>> =
        repository.observeComments(postId)

    fun addComment(postId: String, text: String) {
        viewModelScope.launch {
            repository.addComment(postId, text)
        }
    }
}

