package com.practice.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.newsapp.R
import com.practice.newsapp.databinding.FragmentSearchNewsBinding
import com.practice.newsapp.ui.activities.NewsActivity
import com.practice.newsapp.ui.adapters.NewsAdapter
import com.practice.newsapp.utils.Constants
import com.practice.newsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.practice.newsapp.utils.Resource
import com.practice.newsapp.viewmodal.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        newsAdapter = NewsAdapter()
        newsAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("articleArgs", it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
        }
        binding.apply {
            rvSearchNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@SearchNewsFragment.scrollListener)
            }
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
           job?.cancel()
           job = MainScope().launch {
              delay(SEARCH_NEWS_TIME_DELAY)
              editable?.let {
                 if(editable.toString().isNotEmpty()){
                    newsViewModel.searchNews(editable.toString())
                 }else {
                    newsAdapter.differ.submitList(emptyList())
                 }
              }
           }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if(isLastPage) {
                           binding.rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Timber.tag("Breaking News Frag:").d("An error occurred %s", message)
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading = false
    var isLastPage = false
    var isScroll = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning && isTotalMoreThanVisible && isScroll

            if(shouldPaginate) {
                newsViewModel.searchNews(binding.etSearch.text.toString())
                isScroll = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, newState: Int) {
            super.onScrolled(recyclerView, dx, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScroll = true
            }
        }
    }
}