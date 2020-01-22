package com.amar.data.dao

import androidx.room.*
import com.amar.data.common.ConstantConfig.TABLE_NAME
import com.amar.data.entities.NewsArticle

@Dao
interface ArticleDao {

    /* Top Headline mandatory language=en
    *    1. Country can be in(india) or none for worldwide info
    *    2. Can load by category
    *    3. Load by category and country in(india)
    *  */

    @Query("Select * from $TABLE_NAME where article_type = :type and country = \"all\"")
    fun getAllArticles(type: String = "TOP_HEADLINE"): List<NewsArticle>

    @Query("Select * from $TABLE_NAME")
    fun getNationArticles(): List<NewsArticle>

    @Query("Select * from $TABLE_NAME where article_type = :type and category = :category")
    fun getCategoryArticles(type: String = "TOP_HEADLINE", category: String): List<NewsArticle>

    @Query("Select * from $TABLE_NAME where article_type = :type and category = :category and country = \"in\"")
    fun getCategoryNationArticles(type: String = "TOP_HEADLINE", category: String): List<NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(article: List<NewsArticle>): List<Long>

    @Delete
    fun deleteArticle(article: NewsArticle)

    @Delete
    fun deleteAllArticle(articles: List<NewsArticle>)

}