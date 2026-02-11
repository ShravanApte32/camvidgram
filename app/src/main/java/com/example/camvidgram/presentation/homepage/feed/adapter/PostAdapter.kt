package com.example.camvidgram.presentation.homepage.feed.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                    android.R.drawable.btn_star_big_on
                } else {
                    android.R.drawable.btn_star_big_off
                }
                likeButton.setImageResource(likeIcon)

                // Set click listeners
                profileImage.setOnClickListener {
                    onProfileClick(post.userId)
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