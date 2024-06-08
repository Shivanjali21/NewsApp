package com.practice.newsapp.viewmodal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.newsapp.repository.NewsRepository

class NewsVMProviderFactory(
   private val application: Application,
   private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application, newsRepository) as T
    }
}