package com.amar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amar.data.common.ConstantConfig.TABLE_NAME
import com.amar.data.entities.NewsArticle

@Dao
interface ArticleDao {

    /* Top Headline mandatory language=en
    *    1. All category
    *    2. Can load by category
    *  */

    @Query("Select * from $TABLE_NAME where category = \"all\" ")
    fun getArticles(): LiveData<List<NewsArticle>>

    @Query("Select * from $TABLE_NAME where category = :category order by $TABLE_NAME.time desc")
    fun getCategoryArticles(category: String): LiveData<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(article: List<NewsArticle>): List<Long>

    @Query("Delete from $TABLE_NAME")
    suspend fun deleteAllArticles()

    @Query("Delete from $TABLE_NAME where category = :category")
    suspend fun deleteArticlesByCategory(category: String)

}