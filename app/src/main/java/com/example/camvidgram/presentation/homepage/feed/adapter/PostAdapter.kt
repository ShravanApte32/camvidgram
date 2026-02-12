package com.example.camvidgram.presentation.homepage.feed.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camvidgram.R
import com.example.camvidgram.databinding.ItemPostBinding
import com.example.camvidgram.domain.models.Post

class PostAdapter(
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit,
    private val onShareClick: (Post) -> Unit,
    private val onProfileClick: (String) -> Unit,
    private val onOptionsClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val gestureDetector = android.view.GestureDetector(
            binding.root.context,
            object : android.view.GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val pos = bindingAdapterPosition
                    if (pos == RecyclerView.NO_POSITION) return false

                    val post = getItem(pos)

                    if (!post.isLiked) {
                        onLikeClick(post)
                        animateLike()
                    }
                    return true
                }

            }
        )

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post) {
            binding.apply {
                // Set profile image
                Glide.with(root.context)
                    .load(post.userProfileImage)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(profileImage)

                // Set post image
                Glide.with(root.context)
                    .load(post.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(postImage)

                // Set text content
                usernameText.text = post.username
                locationText.text = post.location ?: ""
                captionText.text = post.caption
                likesCountText.text = "${post.likes} likes"
                commentsCountText.text = "View all ${post.comments} comments"
                timestampText.text = post.formattedTime

                // Set like button state
                val likeIcon = if (post.isLiked) {
                    R.drawable.ic_heart_like
                } else {
                    R.drawable.ic_heart_unlike
                }
                likeButton.setImageResource(likeIcon)

                // Set click listeners
                profileImage.setOnClickListener {
                    onProfileClick(post.userId)
                }

                postImage.setOnTouchListener { _, event ->
                    gestureDetector.onTouchEvent(event)
                    true
                }

                likeButton.setOnClickListener {
                    onLikeClick(post)
                }

                commentButton.setOnClickListener {
                    onCommentClick(post)
                }

                shareButton.setOnClickListener {
                    onShareClick(post)
                }

                moreOptionsButton.setOnClickListener {
                    onOptionsClick(post)
                }
            }
        }

        private fun animateLike() {
            binding.likeOverlay?.apply {
                visibility = View.VISIBLE
                alpha = 0f
                scaleX = 0.5f
                scaleY = 0.5f

                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(180)
                    .withEndAction {
                        animate()
                            .alpha(0f)
                            .setDuration(180)
                            .withEndAction {
                                visibility = View.GONE
                            }
                    }
            }
        }

    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
        return if (oldItem.likes != newItem.likes || oldItem.isLiked != newItem.isLiked) {
            mapOf(
                "likes" to newItem.likes,
                "isLiked" to newItem.isLiked
            )
        } else {
            null
        }
    }
}