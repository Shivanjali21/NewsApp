package com.practice.newsapp.repository

import com.practice.newsapp.api.RetrofitInstance
import com.practice.newsapp.db.ArticleDb
import com.practice.newsapp.models.Article

class NewsRepository (private val newsDb: ArticleDb)  {

  suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
    RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

  suspend fun searchNews(searchQuery:String, pageNo : Int) =
    RetrofitInstance.api.searchForNews(searchQuery, pageNo)

  suspend fun upsert(article: Article) = newsDb.getArticleDao().upsert(article)

  fun getSavedNews() = newsDb.getArticleDao().getAllArticles()

  suspend fun deleteArticle(article: Article) = newsDb.getArticleDao().deleteArticle(article)
}