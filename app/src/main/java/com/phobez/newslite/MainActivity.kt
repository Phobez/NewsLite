package com.phobez.newslite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.phobez.newslite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.topicTitle.text = title

        val articleList = binding.articleList

        val adapter = ArticleListAdapter(this)

        articleList.adapter = adapter

        val model: ArticleViewModel by viewModels()

        model.loadArticles("business")

        model.getArticles().observe(this, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })

        setContentView(binding.root)
    }
}