package com.amar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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


    @Query("Select * from $TABLE_NAME where category = :category")
    fun getCategoryArticles(category: String): LiveData<List<NewsArticle>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(article: List<NewsArticle>): List<Long>


    @Delete
    fun deleteAllArticle(articles: List<NewsArticle>)

    @Query("Delete from $TABLE_NAME where category = \"all\" ")
    fun deleteArticles()

    @Query("Delete from $TABLE_NAME where category = :category")
    fun deleteCategoryArticles(category: String)

}