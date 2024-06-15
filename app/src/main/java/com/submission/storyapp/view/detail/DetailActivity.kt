package com.submission.storyapp.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.submission.storyapp.R
import com.submission.storyapp.data.response.ListStoryItem
import com.submission.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var listStoryItem: ListStoryItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listStoryItem: ListStoryItem? = getListStoryItemFromIntent()

        if (listStoryItem != null) {
            setupUI(listStoryItem)
        } else {
            showError()
        }


    }

    private fun getListStoryItemFromIntent(): ListStoryItem? {
        return if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("listItemStory", ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("listItemStory")
        }
    }

    private fun setupUI(listStoryItem: ListStoryItem) {
        binding.idName.text = listStoryItem.name
        binding.idDescription.text = listStoryItem.description

        Glide
            .with(this)
            .load(listStoryItem.photoUrl)
            .fitCenter()
            .into(binding.imageDetail)
    }

    private fun showError() {
        binding.idName.text = getString(R.string.error_title)
        binding.idDescription.text = getString(R.string.error_description)
    }
}