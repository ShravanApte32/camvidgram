package com.example.camvidgram.presentation.homepage.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camvidgram.R
import com.example.camvidgram.databinding.FragmentFeedBinding
import com.example.camvidgram.domain.models.FeedUiState
import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.presentation.homepage.feed.adapter.PostAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedViewModel by viewModels()

    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupAdapter() {
        postAdapter = PostAdapter(
            onLikeClick = { post ->
                viewModel.likePost(post.id)
            },
            onCommentClick = { post ->
                // Navigate to comments
                navigateToComments(post.id)
            },
            onShareClick = { post ->
                // Share post
                sharePost(post)
            },
            onProfileClick = { userId ->
                // Navigate to profile
                navigateToProfile(userId)
            },
            onOptionsClick = { post ->
                // Show options dialog
                showPostOptions(post)
            }
        )
    }

    private fun setupRecyclerView() {
        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupUI() {
        // Set up pull to refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshPosts()
        }

        binding.retryButton.setOnClickListener {
            viewModel.refreshPosts()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun setupListeners() {
        // Add any additional listeners here
    }

    private fun updateUI(state: FeedUiState) {
        // Handle loading state
        if (state.isLoading) {
            showLoading()
            return
        }

        // Handle error state
        if (state.error != null) {
            showError(state.error)
            return
        }

        // Handle success state
        if (state.posts.isNotEmpty()) {
            showPosts(state.posts)
        } else {
            showEmpty()
        }

        // Hide refresh indicator
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.postsRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
    }

    private fun showError(error: String) {
        binding.errorTextView.text = error
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.postsRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
    }

    private fun showPosts(posts: List<Post>) {
        postAdapter.submitList(posts)
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.postsRecyclerView.visibility = View.VISIBLE
        binding.emptyLayout.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.postsRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
    }

    private fun navigateToComments(postId: String) {
        // TODO: Implement navigation to comments
    }

    private fun sharePost(post: Post) {
        // TODO: Implement share functionality
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, "Check out this post on CamvidGram!")
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "Share post"))
    }

    private fun navigateToProfile(userId: String) {
        // TODO: Implement navigation to profile
    }

    private fun showPostOptions(post: Post) {
        // TODO: Implement options dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
