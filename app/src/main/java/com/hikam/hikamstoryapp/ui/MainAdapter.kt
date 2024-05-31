package com.hikam.hikamstoryapp.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hikam.hikamstoryapp.response.ListStoryItem
import com.hikam.hikamstoryapp.databinding.ItemReviewBinding

class MainAdapter : PagingDataAdapter<ListStoryItem, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review)
        }
    }

    class MyViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.tvItem.text = item.name
            binding.tvDesc.text = item.description
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivItem)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.DETAIL_STORY, item)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity
                ).toBundle()
                itemView.context.startActivity(intent, options)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id // Assuming ListStoryItem has an 'id' property
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
