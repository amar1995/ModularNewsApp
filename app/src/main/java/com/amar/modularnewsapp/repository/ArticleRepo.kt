package com.amar.modularnewsapp.repository

import com.amar.data.dao.ArticleDao
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.modularnewsapp.common.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// ------- This File will interact with separate data module

private const val language = "language"
private const val page = "page"
private const val pageSize = "pageSize"
private const val category = "category"
private const val query = "q"
private const val country = "country"
private const val language_type = "en"
private const val country_type = "in"

class ArticleRepo {

    private var articleDao: ArticleDao
    private var articleService: ArticleService
    // TODO get token from properties file
    private var token: String = "8aefceae6e4e4e2c8ea0364cdf8b5aad"

    private constructor(articleDao: ArticleDao, articleService: ArticleService) {
        this.articleDao = articleDao
        this.articleService = articleService
    }
    companion object {
        private lateinit var articleRepo: ArticleRepo
        fun getInstance(articleDao: ArticleDao, articleService: ArticleService): ArticleRepo {
            if(!::articleRepo.isInitialized) {
                articleRepo = ArticleRepo(articleDao, articleService)
            }
            return articleRepo
        }
    }

    //    suspend fun getTopHeadLine(token: String, options: Map<String, String>) = articleService.getTopHeadLine(token, options)
//    /* Top Headline mandatory language=en
//    *    1. Country can be in(india) or none for worldwide info
//    *    2. Can load by category for world wide
//    *    3. Load by category and country in(india)
//    *  */
//    fun loadHeadlineCountry(): Flow<ViewState<List<NewsArticle>>> {
//        return flow {
//            // loading view
//            emit(ViewState.loading())
//            // Load from db
//            emit(ViewState.success(articleDao.getAllArticles()))
//
//            // server api call
//            val options: HashMap<String,String> = HashMap()
//            options.put(language, language_type)
//            val categoryArticle = articleService.getTopHeadLine(token, options)
//            for(i in 0..categoryArticle.article.size-1) {
//                categoryArticle.article[i].country = "all"
//            }
//            articleDao.insertArticles(categoryArticle.article)
//
//            // Load from db
//            emit(ViewState.success(articleDao.getAllArticles()))
//
//        }.catch {
//            emit(ViewState.error(it.message.orEmpty()))
//        }.flowOn(Dispatchers.IO)
//    }
//
    fun loadHeadlineNation(): Flow<ViewState> {
        return flow {
            // loading view
            emit(ViewState.loading())
            // Load from db
            try {
                emit(ViewState.success<List<NewsArticle>>(articleDao.getNationArticles()))

                // server api call
                val options: HashMap<String, String> = HashMap()
                options.put(language, language_type)

                val categoryArticle = articleService.getTopHeadLine(token, options)

//                    categoryArticle.enqueue(object : Callback<NewsArticleResponse> {
//                        override fun onFailure(call: Call<NewsArticleResponse>, t: Throwable) {
//                            emit(ViewState.error(t.message.toString()))
//                        }
//
//                        override fun onResponse(
//                            call: Call<NewsArticleResponse>,
//                            response: Response<NewsArticleResponse>
//                        ) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                    })

                if(categoryArticle.isSuccessful) {
                    val articleBody = categoryArticle.body()
                    for (i in 0..articleBody!!.article.size - 1) {
                        articleBody.article[i].country = country_type
                    }
                    println("Data: " + articleBody)
                    println("Save list: " + articleDao.insertArticles((articleBody.article)))
                    // Load from db
                    val data = articleDao.getNationArticles()
                    println("database data: " + data )
                    emit(ViewState.success<List<NewsArticle>>(data))
                } else {
                    emit(ViewState.error("Http error code " + categoryArticle.code()))
                }
//                val categoryArticle = liveData(Dispatchers.IO) {
//                    val response = articleService.getTopHeadLine(token, options)
//                    if(response.)
//                }.value
//                emit(categoryArticle)
//                categoryArticle.enqueue()




            } catch(e: Exception) {
                emit(ViewState.error(e.message.orEmpty()))
            }
        }.flowOn(Dispatchers.IO)
    }


//    fun loadByCategory(categoryType: String): Flow<ViewState<List<NewsArticle>>> {
//        return flow {
//            // loading view
//            emit(ViewState.loading())
//            // Load from db
//            emit(ViewState.success(articleDao.getCategoryArticles(category = categoryType)))
//
//            // server api call
//            val options: HashMap<String,String> = HashMap()
//            options.put(language, language_type)
//            options.put(category,categoryType)
//            val categoryArticle = articleService.getTopHeadLine(token, options)
//            for(i in 0..categoryArticle.article.size-1) {
//                categoryArticle.article[i].category = categoryType
//                categoryArticle.article[i].country = "all"
//            }
//            articleDao.insertArticles(categoryArticle.article)
//
//            // Load from db
//            emit(ViewState.success(articleDao.getCategoryArticles(category = categoryType)))
//
//        }.catch {
//            emit(ViewState.error(it.message.orEmpty()))
//        }.flowOn(Dispatchers.IO)
//    }

//    fun loadByCategoryCountry(categoryType: String): Flow<ViewState<List<NewsArticle>>> {
//        return flow {
//            // loading view
//            emit(ViewState.loading())
//            // Load from db
//            try {
//                emit(ViewState.success(articleDao.getCategoryNationArticles(category = categoryType)))
//
//                // server api call
//                val options: HashMap<String, String> = HashMap()
//                options.put(language, language_type)
//                options.put(category, categoryType)
//                options.put(country, country_type)
//                val categoryArticle = articleService.getTopHeadLine(token, options)
//                for (i in 0..categoryArticle.article.size - 1) {
//                    categoryArticle.article[i].category = categoryType
//                    categoryArticle.article[i].country = country_type
//                }
//                articleDao.insertArticles(categoryArticle.article)
//
//                // Load from db One source of truth
//                emit(ViewState.success(articleDao.getCategoryNationArticles(category = categoryType)))
//
//            } catch(e: Exception) {
//                emit(ViewState.error(e.message.orEmpty()))
//            }
//        }
////        .flowOn(Dispatchers.IO)
//    }

    /* get everything with query-search and language=en
    *   1. query to top headline(No database require only work with search live-search)
    * */
}