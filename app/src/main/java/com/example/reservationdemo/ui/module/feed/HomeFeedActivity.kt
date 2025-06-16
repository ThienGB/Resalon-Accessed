package com.example.reservationdemo.ui.module.feed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reservationdemo.databinding.ActivityHomeLayoutBinding
import com.example.reservationdemo.ui.adapter.FeedAdapter

class HomeFeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityHomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummyData = listOf("Bài viết 1", "Bài viết 2", "Bài viết 3")
        val adapter = FeedAdapter(dummyData)

        binding.recyclerFeed.layoutManager = LinearLayoutManager(this)
        binding.recyclerFeed.adapter = adapter
    }
}
