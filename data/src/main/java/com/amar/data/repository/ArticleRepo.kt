package com.amar.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.amar.data.DatabaseClient
import com.amar.data.common.AbsentLiveData
import com.amar.data.common.ConstantConfig.PAGE_SIZE
import com.amar.data.common.InternetConnection
import com.amar.data.common.NetworkManager
import com.amar.data.dao.ArticleDao
import com.amar.data.entities.NewsArticle
import com.amar.data.entities.NewsArticleResponse
import com.amar.data.service.ApiResponse
import com.amar.data.service.ArticleService
import com.amar.data.vo.Resource

// ------- This File will interact with separate data module

private const val language = "language"
private const val page = "page"
private const val pageSize = "pageSize"
private const val CATEGORY = "category"
private const val QUERY = "q"
private const val language_type = "en"


class ArticleRepo(
    private val databaseClient: DatabaseClient,
    private val articleService: ArticleService,
    private val context: Context
) {

    private var articleDao: ArticleDao

    init {
        articleDao = databaseClient.articleDao()
    }

    companion object {
        private lateinit var articleRepo: ArticleRepo
        fun getInstance(
            databaseClient: DatabaseClient,
            articleService: ArticleService,
            context: Context
        ): ArticleRepo {
            if (!::articleRepo.isInitialized) {
                articleRepo = ArticleRepo(databaseClient, articleService, context)
            }
            return articleRepo
        }
    }

    fun loadData(pageNo: Int, category: String): LiveData<Resource<List<NewsArticle>>> {
        return object : NetworkManager<List<NewsArticle>, NewsArticleResponse?>() {
            override fun shouldFetch(data: List<NewsArticle>?): Boolean {
                return (data == null || data.isEmpty() || data.size < PAGE_SIZE * (pageNo + 1))
            }

            override fun loadFromDb(): LiveData<List<NewsArticle>> =
                articleDao.getCategoryArticles(category)

            override suspend fun saveCallResult(item: NewsArticleResponse?) {
                val articleList = item!!.article
                for (i in 0..articleList.size - 1) {
                    articleList[i].category = category
                }
                articleDao.insertArticles(articleList)
            }

            override fun createCall(): LiveData<ApiResponse<NewsArticleResponse?>> {
                val options: HashMap<String, String> = HashMap()
                options.put(pageSize, PAGE_SIZE.toString())
                options.put(page, pageNo.toString())
                options.put(CATEGORY, category)
                return articleService.getArticle(options)
            }

            override fun isInternetAvailable(): Boolean = InternetConnection.isAvailable(context)

            // database is also provided so it is both way local db and network.
            override fun isOnlineRequest(): Boolean = false
        }.asLiveData()
    }

    fun searchData(pageNo: Int, query: String): LiveData<Resource<List<NewsArticle>>> {
        return object : NetworkManager<List<NewsArticle>, NewsArticleResponse?>() {
            // always fetch it
            override fun shouldFetch(data: List<NewsArticle>?): Boolean = true

            override fun loadFromDb(): LiveData<List<NewsArticle>> =
                AbsentLiveData.create()

            override suspend fun saveCallResult(item: NewsArticleResponse?) {
                // do nothing
                // no need to save searched data
            }

            override fun createCall(): LiveData<ApiResponse<NewsArticleResponse?>> {
                val options: HashMap<String, String> = HashMap()
                options.put(pageSize, PAGE_SIZE.toString())
                options.put(page, pageNo.toString())
                options.put(QUERY, query)
                return articleService.getArticle(options)
            }

            override fun isInternetAvailable(): Boolean = InternetConnection.isAvailable(context)

            // this is only networked call
            override fun isOnlineRequest(): Boolean = true
        }.asLiveData()
    }

    suspend fun deleteAllArticles() {
        articleDao.deleteAllArticles()
    }

    suspend fun deleteArticlesByCategory(category: String) {
        articleDao.deleteArticlesByCategory(category = category)
    }
}

/* get everything with query-search and language=en
*   1. query to top headline(No database require only work with search live-search)
* */