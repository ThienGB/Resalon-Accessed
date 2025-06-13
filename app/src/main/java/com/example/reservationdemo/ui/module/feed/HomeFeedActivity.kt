package com.example.reservationdemo.ui.module.feed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservationdemo.R
import com.example.reservationdemo.ui.adapter.FeedAdapter

class HomeFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.home_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerFeed)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dummyData = listOf("Bài viết 1", "Bài viết 2", "Bài viết 3")
        val adapter = FeedAdapter(dummyData)

        recyclerView.adapter = adapter
    }
}
