package com.phobez.newslite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phobez.newslite.databinding.ArticleListItemBinding

class ArticleListAdapter(private val context: Context) :
    RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>() {

    private var data: List<Article> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = data[position]

        holder.articleTitle.text = item.title
        holder.articleInfo.text = item.getInfo()
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = data.size

    fun setData(data: List<Article>) {
        if (this.data.isEmpty()) {
            this.data = data
        }
    }

    class ArticleViewHolder(binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val articleTitle = binding.articleTitle
        val articleInfo = binding.articleInfo
    }

}