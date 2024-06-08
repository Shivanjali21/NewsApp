package com.practice.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import android.widget.Button
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.practice.newsapp.R
import com.practice.newsapp.databinding.FragmentArticleBinding
import com.practice.newsapp.ui.activities.NewsActivity
import com.practice.newsapp.viewmodal.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.articleArgs
        binding.apply {
           webView.apply {
              webViewClient = WebViewClient()
              loadUrl(article.url!!)
           }

           fab.setOnClickListener {
              newsViewModel.saveArticle(article)
              Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
           }
        }
    }
}

