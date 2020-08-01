package com.amar.modularnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.common.AbsentLiveData
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.data.repository.ArticleRepo
import com.amar.data.repository.PageSize
import com.amar.data.vo.Resource
import com.amar.data.vo.Status
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class ArticleCache {
    // id, data
    var articleList: LinkedHashMap<String, NewsArticle> = LinkedHashMap()
    private var offset: Int = 0
    private var initialOffset: Int = 0

    fun init() {
        this.offset = 0
        this.articleList = linkedMapOf()
        this.initialOffset = 0
    }

    fun refresh() {
        this.offset = 0
        this.initialOffset = 0
        articleList.clear()
    }

    fun updateOffset() {
        this.offset = this.initialOffset + 1
    }

    fun updateInitialOffset() {
        this.initialOffset = this.offset
    }

    fun clearOffset() {
        this.offset = 0
        this.initialOffset = 0
    }

    fun getOffset(): Int {
        return this.offset
    }
}

enum class Category {
    general,
    business,
    health,
    sports,
    science,
    technology,
    entertainment
}

object NewsType {
    var offset: Int = 0
    var initialOffset: Int = 0
    var category: Category = Category.general
    var generalArticleCache = ArticleCache()
    var sportsArticleCache = ArticleCache()
    var businessArticleCache = ArticleCache()
    var healthArticleCache = ArticleCache()
    var scienceArticleCache = ArticleCache()
    var entertainmentArticleCache = ArticleCache()
    var technologyArticleCache = ArticleCache()
}

class ArticleModel(application: Application) : AndroidViewModel(application) {
    val articleRepo: ArticleRepo by lazy {
        ArticleRepo.getInstance(
            DatabaseClient.getInstance(application),
            APIClient.retrofitServiceProvider(),
            application.applicationContext
        )
    }
    private var _articles: MutableLiveData<NewsType> = MutableLiveData()

    init {
        _articles.postValue(NewsType)
    }

    fun endOfPage(screen: Screen) {
        println("is end of page called >>>>")
        when (screen) {
            Screen.GENERAL -> {
                _articles.value = _articles.value?.also {
                    it.generalArticleCache.updateOffset()
                }
            }
            else -> {
                _articles.value = _articles.value?.also {
                    it.generalArticleCache.updateOffset()
                }
            }
        }
    }

    val articles = _articles.switchMap {
        when (it.category) {
            Category.general -> {
                println("is getting here >>>>")
                recieveGeneralArticleData(it)
            }
            else -> {
                recieveGeneralArticleData(it)
            }
        }
    }

    private fun recieveGeneralArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.general.name,
            pageNo = news.generalArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.generalArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.generalArticleCache.articleList
                            ),
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                }
                Status.SUCCESS -> {
                    if (data.data.isNullOrEmpty()) {
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.generalArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.generalArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.generalArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.generalArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = false
                            )
                        )
                    }
                }
                else -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Error(Throwable("Unknown Error")),
                            hasLoadedAllPages = false,
                            isLoadingMorePage = false
                        )
                    )
                }
            }
        }
    }
}

data class ArticleViewState(
    val articleState: ArticleState,
    val isLoadingMorePage: Boolean,
    val hasLoadedAllPages: Boolean
)

sealed class ArticleState {
    data class Success(val articles: Map<String, NewsArticle>) : ArticleState()
    object Loading : ArticleState()
    data class Error(val throwable: Throwable) : ArticleState()
}
