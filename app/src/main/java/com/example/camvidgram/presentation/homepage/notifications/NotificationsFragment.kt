package com.example.camvidgram.presentation.homepage.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camvidgram.R
import com.example.camvidgram.data.local.db.AppDatabase
import com.example.camvidgram.data.repository.NotificationRepository
import com.example.camvidgram.domain.models.NotificationItemModel
import com.example.camvidgram.presentation.homepage.notifications.adapter.NotificationsAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: NotificationsAdapter
    private lateinit var repository: NotificationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.notifications_recycler_view)
        tabLayout = view.findViewById(R.id.tab_layout)

        // Initialize Room and Repository
        val database = AppDatabase.getInstance(requireContext())
        repository = NotificationRepository(database)

        setupRecyclerView()
        setupTabLayout()

        // Load notifications from Room
        loadNotifications()

        // Add dummy data for testing (remove in production)
        lifecycleScope.launch {
            repository.addDummyNotifications()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationsAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadNotifications() // All notifications
                    1 -> loadFollowingNotifications() // Following only
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadNotifications() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.getNotificationsStream().collect { items ->
                    adapter.updateItems(items)
                }
            }
        }
    }

    private fun loadFollowingNotifications() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.getFollowingNotificationsStream().collect { items ->
                    adapter.updateItems(items)
                }
            }
        }
    }
}