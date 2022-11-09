package com.example.storyapp.ui.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import com.example.storyapp.utils.convertISOTimeToDate

class StoryDetailActivity : AppCompatActivity() {
    private var _binding: ActivityStoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        val imgView = binding.imageView
        val tvTitle = binding.tvTitle
        val tvDate = binding.tvDate
        val tvDescription = binding.tvDescription

        val photoUrl = intent.getStringExtra(DETAIL_PHOTO)
        val title = intent.getStringExtra(DETAIL_TITLE)
        val date = intent.getStringExtra(DETAIL_DATE)
        val description = intent.getStringExtra(DETAIL_DESCRIPTION)

        imgView.loadImage(photoUrl.toString())
        val strDate = convertISOTimeToDate(date.toString())

        tvTitle.text = title.toString()
        tvDate.text = strDate
        tvDescription.text = description.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(1000, 500))
            .centerCrop()
            .into(this)
    }

    companion object {
        val DETAIL_PHOTO = "detail_photo"
        val DETAIL_TITLE = "detail_title"
        val DETAIL_DATE = "detail_date"
        val DETAIL_DESCRIPTION = "detail_description"
    }
}