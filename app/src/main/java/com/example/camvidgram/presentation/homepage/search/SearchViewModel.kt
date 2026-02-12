package com.example.camvidgram.presentation.homepage.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.models.SearchPost
import com.example.camvidgram.domain.repository.PostRepository
import com.example.camvidgram.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _allPosts = MutableStateFlow<List<SearchPost>>(emptyList())

    val filteredPosts: StateFlow<List<SearchPost>> =
        combine(_query, _allPosts) { query, posts ->
            if (query.isBlank()) posts
            else posts.filter {
                it.username.contains(query, true) ||
                        it.caption.contains(query, true)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            searchRepository.observeSearchPosts().collect {
                _allPosts.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _query.value = query
    }
}

