package com.submission.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.storyapp.R
import com.submission.storyapp.data.response.ListStoryItem
import com.submission.storyapp.databinding.ItemStoryBinding
import com.submission.storyapp.view.detail.DetailActivity

class UserListLocationAdapter : PagingDataAdapter<ListStoryItem, UserListLocationAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder (private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        private var idStoryImage: ImageView = itemView.findViewById(R.id.id_story_image)
        private var idText: TextView = itemView.findViewById(R.id.id_name)
        private var idDescription: TextView = itemView.findViewById(R.id.id_description)
        fun bind(listStoryItem: ListStoryItem) {
            binding.idName.text = listStoryItem.name
            binding.idDescription.text = listStoryItem.description

            Glide
                .with(itemView.context)
                .load(listStoryItem.photoUrl)
                .fitCenter()
                .into(binding.idStoryImage)

            binding.itemStory.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("listItemStory", listStoryItem)

                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(idStoryImage, "photo"),
                        Pair(idText, "name"),
                        Pair(idDescription, "description")
                    )
                itemView.context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        return ListViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}