package com.practice.newsapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practice.newsapp.R
import com.practice.newsapp.databinding.ActivityNewsBinding
import com.practice.newsapp.db.ArticleDb
import com.practice.newsapp.repository.NewsRepository
import com.practice.newsapp.viewmodal.NewsVMProviderFactory
import com.practice.newsapp.viewmodal.NewsViewModel

class NewsActivity : AppCompatActivity() {

    private val binding: ActivityNewsBinding by lazy {
      ActivityNewsBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    lateinit var newsViewModel : NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val newRepo = NewsRepository(ArticleDb(this))
        val viewModelProviderFactory = NewsVMProviderFactory(application, newRepo)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        binding.apply {
           val navHost = supportFragmentManager.findFragmentById(R.id.newsFCV) as NavHostFragment
           navController = navHost.navController
           bottomNV.setupWithNavController(navController = navController)
        }
    }
}