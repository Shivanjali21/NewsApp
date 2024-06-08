package com.practice.newsapp.db

import androidx.room.TypeConverter
import com.practice.newsapp.models.Source

class NewsConverters {

    @TypeConverter
    fun fromSource(source: Source) : String {
      return source.name
    }

    @TypeConverter
    fun toSource(name: String) : Source {
      return Source(name, name)
    }
}