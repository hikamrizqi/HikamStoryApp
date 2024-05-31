package com.hikam.hikamstoryapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hikam.hikamstoryapp.response.ListStoryItem
import com.hikam.hikamstoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY)
        detail?.let { setupAction(it) }

        supportActionBar?.apply {
            show()
            title = "Detail Story"
        }
    }

    private fun setupAction(detail: ListStoryItem) {
        Glide.with(this)
            .load(detail.photoUrl)
            .into(binding.avatarDetail)
        binding.tvName.text = detail.name
        binding.tvDesc.text = detail.description
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}
