package com.example.camvidgram.domain.usecases

import com.example.camvidgram.domain.models.SearchPost
import com.example.camvidgram.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LoadSearchPostUseCase @Inject constructor() : SearchRepository {
    override fun observeSearchPosts(): Flow<List<SearchPost>> =
        flowOf(FakeSearchPosts.posts)
}

object FakeSearchPosts {

    val posts = listOf(
        SearchPost("1", "https://picsum.photos/300/300?random=101", "john_doe", "Morning vibes ☀️"),
        SearchPost("2", "https://picsum.photos/300/300?random=102", "travelwithme", "Goa trip 🌴"),
        SearchPost("3", "https://picsum.photos/300/300?random=103", "foodie_king", "Pizza is life 🍕"),
        SearchPost("4", "https://picsum.photos/300/300?random=104", "streetshots", "Mumbai streets 📸"),
        SearchPost("5", "https://picsum.photos/300/300?random=105", "dev_shravan", "Android grind 💻"),
        SearchPost("6", "https://picsum.photos/300/300?random=106", "memes_daily", "Tag your friend 😂"),
        SearchPost("7", "https://picsum.photos/300/300?random=107", "city_life", "Night lights 🌃"),
        SearchPost("8", "https://picsum.photos/300/300?random=108", "wanderer", "Solo travel ✈️"),
        SearchPost("9", "https://picsum.photos/300/300?random=109", "nature_love", "Peace 🍃")
    )
}
