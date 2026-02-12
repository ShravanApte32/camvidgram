package com.example.camvidgram.domain.repository


import com.example.camvidgram.domain.models.SearchPost
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun observeSearchPosts(): Flow<List<SearchPost>>
}