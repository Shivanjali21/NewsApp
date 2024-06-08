package com.practice.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practice.newsapp.databinding.ItemArticlePreviewBinding
import com.practice.newsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleVH>() {

    var onItemClick: ((Article) -> Unit)? = null

    inner class ArticleVH(val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
          return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
          return oldItem.url == newItem.url
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ArticleVH {
        val vh = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleVH(vh)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ArticleVH, position: Int) {
        val articleData = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context).load(articleData.urlToImage).into(ivArticleImage)
            tvSource.text = articleData.source?.name
            tvTitle.text = articleData.title
            tvDescription.text = articleData.description
            tvPublishedAt.text = articleData.publishedAt

            mcvNewsRoot.setOnClickListener {
                onItemClick?.invoke(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}