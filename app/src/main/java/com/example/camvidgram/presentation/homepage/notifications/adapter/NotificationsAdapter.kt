package com.example.camvidgram.presentation.homepage.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camvidgram.R
import com.example.camvidgram.domain.models.NotificationItemModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class NotificationsAdapter(
    private var items: List<NotificationItemModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_NOTIFICATION = 1
    }

    fun updateItems(newItems: List<NotificationItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is NotificationItemModel.Header -> TYPE_HEADER
            is NotificationItemModel.Notification -> TYPE_NOTIFICATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_section_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification, parent, false)
                NotificationViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is NotificationItemModel.Header -> (holder as HeaderViewHolder).bind(item)
            is NotificationItemModel.Notification -> (holder as NotificationViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.section_header_text)

        fun bind(header: NotificationItemModel.Header) {
            headerText.text = header.title
        }
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ShapeableImageView = itemView.findViewById(R.id.user_avatar)
        private val notificationText: TextView = itemView.findViewById(R.id.notification_text)
        private val timeText: TextView = itemView.findViewById(R.id.notification_time)
        private val postImage: ImageView = itemView.findViewById(R.id.notification_image)
        private val followButton: MaterialButton = itemView.findViewById(R.id.follow_button)
        private val unreadDot: View = itemView.findViewById(R.id.unread_dot)

        fun bind(notification: NotificationItemModel.Notification) {
            // Set notification text with bold username
            val fullText = "<b>${notification.username}</b> ${notification.actionText}"
            notificationText.text = android.text.Html.fromHtml(fullText, android.text.Html.FROM_HTML_MODE_LEGACY)

            timeText.text = notification.timeAgo

            // Show/hide unread dot
            unreadDot.visibility = if (notification.isUnread) View.VISIBLE else View.GONE

            // Load avatar with Glide
            if (notification.avatarUrl != null) {
                Glide.with(itemView.context)
                    .load(notification.avatarUrl)
                    .placeholder(R.drawable.ic_user)
                    .circleCrop()
                    .into(avatar)
            } else {
                avatar.setImageResource(R.drawable.ic_user)
            }

            // Handle post thumbnail
            if (notification.postThumbnailUrl != null) {
                postImage.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(notification.postThumbnailUrl)
                    .placeholder(R.drawable.ic_user)
                    .centerCrop()
                    .into(postImage)
            } else {
                postImage.visibility = View.GONE
            }

            // Handle follow button
            if (notification.isFollowRequest) {
                followButton.visibility = View.VISIBLE
                followButton.setOnClickListener {
                    followButton.text = "Following"
                    followButton.isEnabled = false
                }
            } else {
                followButton.visibility = View.GONE
            }
        }
    }
}