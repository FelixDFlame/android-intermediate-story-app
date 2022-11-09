package com.example.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.database.StoryItem
import com.example.storyapp.databinding.ItemStoryCardBinding
import com.example.storyapp.ui.storydetail.StoryDetailActivity
import com.example.storyapp.utils.convertISOTimeToDate

class ListStoryAdapter :
    PagingDataAdapter<StoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(private var binding: ItemStoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgPhoto: ImageView = binding.imgItemPhoto
        val tvTitle: TextView = binding.tvItemTitle
        val tvDate: TextView = binding.tvItemDate

        fun bind(story: StoryItem) {
            imgPhoto.loadImage(story.photoUrl)
            val date = convertISOTimeToDate(story.createdAt)

            tvTitle.text = story.name
            tvDate.text = date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            val itemView = holder.itemView
            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair.create(holder.imgPhoto, "photo"),
                        androidx.core.util.Pair.create(holder.tvTitle, "name"),
                        androidx.core.util.Pair.create(holder.tvDate, "date"),
                    )
                startStoryDetailActivity(itemView, story, optionsCompat)
            }
        }
    }

    private fun startStoryDetailActivity(
        itemView: View,
        data: StoryItem,
        optionsCompat: ActivityOptionsCompat
    ) {
        val intent = Intent(itemView.context, StoryDetailActivity::class.java)
        intent.putExtra(StoryDetailActivity.DETAIL_PHOTO, data.photoUrl)
        intent.putExtra(StoryDetailActivity.DETAIL_TITLE, data.name)
        intent.putExtra(StoryDetailActivity.DETAIL_DATE, data.createdAt)
        intent.putExtra(StoryDetailActivity.DETAIL_DESCRIPTION, data.description)
        itemView.context.startActivity(intent, optionsCompat.toBundle())
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}

private fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().override(1000, 500))
        .centerCrop()
        .into(this)
}
