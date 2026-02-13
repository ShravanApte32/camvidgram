package com.example.camvidgram.data.repository

import com.example.camvidgram.data.local.db.AppDatabase
import com.example.camvidgram.data.local.entities.ActionType
import com.example.camvidgram.data.local.entities.NotificationEntity
import com.example.camvidgram.domain.models.NotificationItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val database: AppDatabase
) {

    fun getNotificationsStream(): Flow<List<NotificationItemModel>> {
        return database.notificationDao().getAllNotifications()
            .map { entities ->
                groupNotificationsByDate(entities)
            }
    }

    fun getFollowingNotificationsStream(): Flow<List<NotificationItemModel>> {
        return database.notificationDao().getFollowingNotifications()
            .map { entities ->
                groupNotificationsByDate(entities)
            }
    }

    private fun groupNotificationsByDate(notifications: List<NotificationEntity>): List<NotificationItemModel> {
        val groupedItems = mutableListOf<NotificationItemModel>()
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Today
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.time

        // This Week
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val thisWeek = calendar.time

        // This Month
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -1)
        val thisMonth = calendar.time

        val todayNotifications = notifications.filter { it.timestamp >= yesterday.time }
        val thisWeekNotifications = notifications.filter {
            it.timestamp < yesterday.time && it.timestamp >= thisWeek.time
        }
        val thisMonthNotifications = notifications.filter {
            it.timestamp < thisWeek.time && it.timestamp >= thisMonth.time
        }
        val earlierNotifications = notifications.filter {
            it.timestamp < thisMonth.time
        }

        if (todayNotifications.isNotEmpty()) {
            groupedItems.add(NotificationItemModel.Header("Today"))
            groupedItems.addAll(todayNotifications.map { it.toPresentationModel() })
        }

        if (thisWeekNotifications.isNotEmpty()) {
            groupedItems.add(NotificationItemModel.Header("This Week"))
            groupedItems.addAll(thisWeekNotifications.map { it.toPresentationModel() })
        }

        if (thisMonthNotifications.isNotEmpty()) {
            groupedItems.add(NotificationItemModel.Header("This Month"))
            groupedItems.addAll(thisMonthNotifications.map { it.toPresentationModel() })
        }

        if (earlierNotifications.isNotEmpty()) {
            groupedItems.add(NotificationItemModel.Header("Earlier"))
            groupedItems.addAll(earlierNotifications.map { it.toPresentationModel() })
        }

        return groupedItems
    }

    private fun NotificationEntity.toPresentationModel(): NotificationItemModel.Notification {
        val timeAgo = getTimeAgo(this.timestamp)
        val actionText = when (this.actionType) {
            ActionType.LIKE -> "liked your photo."
            ActionType.COMMENT -> "commented: \"${this.commentText ?: ""}\""
            ActionType.FOLLOW -> "started following you."
            ActionType.FOLLOW_REQUEST -> "requested to follow you."
            ActionType.MENTION -> "mentioned you in a comment."
            ActionType.TAG -> "tagged you in a photo."
        }

        return NotificationItemModel.Notification(
            id = this.id,
            username = this.username,
            actionText = actionText,
            timeAgo = timeAgo,
            avatarUrl = this.userAvatarUrl,
            postThumbnailUrl = this.postThumbnailUrl,
            isFollowRequest = this.actionType == ActionType.FOLLOW_REQUEST,
            isUnread = !this.isRead
        )
    }

    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60 * 1000 -> "now"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}d"
            diff < 30 * 24 * 60 * 60 * 1000 -> "${diff / (7 * 24 * 60 * 60 * 1000)}w"
            else -> "${diff / (30 * 24 * 60 * 60 * 1000)}mo"
        }
    }

    suspend fun addDummyNotifications() = withContext(Dispatchers.IO) {
        val dummyNotifications = listOf(
            NotificationEntity(
                id = "123456",
                username = "james_rodriguez",
                userAvatarUrl = "https://i.pravatar.cc/150?img=1",
                actionType = ActionType.LIKE,
                actionText = "liked your photo",
                postId = "post123",
                postThumbnailUrl = "https://picsum.photos/200/200?random=1",
                timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000), // 2h ago
                isRead = false
            ),
            NotificationEntity(
                id = "654321",
                username = "sarah_johnson",
                userAvatarUrl = "https://i.pravatar.cc/150?img=2",
                actionType = ActionType.FOLLOW,
                actionText = "started following you",
                timestamp = System.currentTimeMillis() - (5 * 60 * 60 * 1000), // 5h ago
                isRead = false
            ),
            NotificationEntity(
                id = "162534",
                username = "robert_downey",
                userAvatarUrl = "https://i.pravatar.cc/150?img=5",
                actionType = ActionType.FOLLOW_REQUEST,
                actionText = "requested to follow you",
                timestamp = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000), // 3d ago
                isRead = true
            ),
            // Add more as needed
        )

        database.notificationDao().insertAllNotifications(dummyNotifications)
    }

    suspend fun markAsRead(notificationId: String) {
        database.notificationDao().markAsRead(notificationId)
    }

    suspend fun markAllAsRead() {
        database.notificationDao().markAllAsRead()
    }
}