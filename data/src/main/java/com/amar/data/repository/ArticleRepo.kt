package com.amar.data.repository

import androidx.compose.Context
import androidx.lifecycle.LiveData
import com.amar.data.DatabaseClient
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
private const val category = "category"
private const val query = "q"
private const val language_type = "en"


object PageSize {
    var topHeadlineInternationalPageNo: Int = 1
    var categoryInternational: Category = Category
}


object Category {
    var category: String = ""
    var pageNo: Int = 1
}

class ArticleRepo(
    private val databaseClient: DatabaseClient,
    private val articleService: ArticleService,
    private val context: Context
) {

    private var articleDao: ArticleDao
    // TODO get token from properties file
    private var token: String = "8aefceae6e4e4e2c8ea0364cdf8b5aad"

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

    val internationalHeadline: LiveData<List<NewsArticle>> = articleDao.getArticles()

    fun getCategoryInternationalHeadline(category: String): LiveData<List<NewsArticle>> {
        return articleDao.getCategoryArticles(category)
    }

//    /* Top Headline mandatory language=en
//    *    1. Country can be in(india) or none for worldwide info
//    *    2. Can load by category for world wide
//    *    3. Load by category and country in(india)
//    *  */


//     fun loadMoreData(pageNo: Int) {
//        val options: HashMap<String, String> = HashMap()
//        options.put(language, language_type)
//        options.put(pageSize, "10")
//        options.put(page, pageNo.toString())
//        var internationalArticle: NewsArticleResponse? = null
//        runBlocking {
//            launch(Dispatchers.IO) {
//                internationalArticle = articleService.getTopHeadLine(options)
//            }
//        }
////        Not working with compose
////        withContext(Dispatchers.IO) {
////            println("Load more data server " + internationalArticle)
////        }
//
//        if (internationalArticle != null) {
//            DatabaseClient.databaseWriteExecutor.execute(Runnable {
//                val articles = internationalArticle!!.article
//                for (i in 0..articles.size - 1) {
//                    articles[i].category = "all"
//                }
//                articleDao.insertArticles(internationalArticle!!.article)
//            })
//        }
//    }
//
//
//    suspend fun refreshData() {
//        DatabaseClient.databaseWriteExecutor.execute(Runnable {
//            articleDao.deleteArticles()
//        })
//        val options: HashMap<String, String> = HashMap()
//        options.put(language, language_type)
//        options.put(pageSize, "10")
//        options.put(page, "1")
//        var internationalArticle: NewsArticleResponse? = null
//        withContext(Dispatchers.IO) {
//            internationalArticle = articleService.getTopHeadLine(options)
//        }
//        if (internationalArticle != null) {
//            DatabaseClient.databaseWriteExecutor.execute(Runnable {
//                val articles = internationalArticle!!.article
//                for (i in 0..articles.size - 1) {
//                    articles[i].category = "all"
//                }
//                articleDao.insertArticles(articles)
//            })
//        }
//    }

    fun loadData(pageNo: Int): LiveData<Resource<List<NewsArticle>>> {
        return object: NetworkManager<List<NewsArticle>, NewsArticleResponse?>() {

            override fun shouldFetch(data: List<NewsArticle>?): Boolean {
                return (data == null || data.isEmpty() || data.size < 10*pageNo) && InternetConnection.isAvailable(context = context)
            }

            override fun loadFromDb(): LiveData<List<NewsArticle>> = articleDao.getArticles()

//            override fun createCall(): LiveData<APIResponse<NewsArticle>> {
//                val options: HashMap<String, String> = HashMap()
//                options.put(language, language_type)
//                options.put(pageSize, "10")
//                options.put(page, pageNo.toString())
//                var internationalArticle: NewsArticleResponse? = null
//                runBlocking {
//                    launch(Dispatchers.IO) {
//                        internationalArticle = articleService.getTopHeadLine(token, options)
//                    }
//                }
//
//            }

            override suspend fun saveCallResult(item: NewsArticleResponse?) {
                val articleList = item!!.article
                for(i in 0..articleList.size-1) {
                    articleList[i].category = "all"
                }
                articleDao.insertArticles(articleList)
            }

            override fun createCall(): LiveData<ApiResponse<NewsArticleResponse?>> {
                val options: HashMap<String, String> = HashMap()
                options.put(language, language_type)
                options.put(pageSize, "10")
                options.put(page, pageNo.toString())
                return articleService.getArticle2(options)
            }
        }.asLiveData()
    }

    /* get everything with query-search and language=en
    *   1. query to top headline(No database require only work with search live-search)
    * */
}