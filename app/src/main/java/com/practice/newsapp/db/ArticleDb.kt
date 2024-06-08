package com.practice.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practice.newsapp.models.Article

@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(NewsConverters::class)
abstract class ArticleDb : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDataBase(context).also { instance = it }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDb::class.java,
                "article.db"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}