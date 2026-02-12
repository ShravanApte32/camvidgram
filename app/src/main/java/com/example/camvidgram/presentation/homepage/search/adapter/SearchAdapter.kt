package com.example.camvidgram.presentation.homepage.search.adapter

import android.R.attr.onClick
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.example.camvidgram.databinding.ItemSearchPostBinding
import com.example.camvidgram.domain.models.Post
import com.example.camvidgram.domain.models.SearchPost

class SearchAdapter(
    private val onPostClick: (SearchPost) -> Unit
) : ListAdapter<SearchPost, SearchAdapter.SearchViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: SearchPost) {
            Glide.with(binding.root.context)
                .load(post.imageUrl)
                .into(binding.postImage)

//            binding.root.setOnClickListener {
//                onClick(post)
//            }
        }
    }

    class Diff : DiffUtil.ItemCallback<SearchPost>() {
        override fun areItemsTheSame(oldItem: SearchPost, newItem: SearchPost) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchPost, newItem: SearchPost) =
            oldItem == newItem
    }
}


