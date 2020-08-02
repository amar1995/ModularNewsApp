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
import com.amar.data.repository.ArticleRepo
import com.amar.data.vo.Resource
import com.amar.data.vo.Status
import com.amar.modularnewsapp.ui.Screen

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

    fun updateCategory(category: Category) {
        this.category = category
    }
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
            Screen.BUSINESS -> {
                _articles.value = _articles.value?.also {
                    it.businessArticleCache.updateOffset()
                }
            }
            Screen.HEALTH -> {
                _articles.value = _articles.value?.also {
                    it.healthArticleCache.updateOffset()
                }
            }
            Screen.ENTERTAINMENT -> {
                _articles.value = _articles.value?.also {
                    it.entertainmentArticleCache.updateOffset()
                }
            }
            Screen.SCIENCE -> {
                _articles.value = _articles.value?.also {
                    it.scienceArticleCache.updateOffset()
                }
            }
            Screen.SPORTS -> {
                _articles.value = _articles.value?.also {
                    it.sportsArticleCache.updateOffset()
                }
            }
            Screen.TECHNOLOGY -> {
                _articles.value = _articles.value?.also {
                    it.technologyArticleCache.updateOffset()
                }
            }
        }
    }

    fun updateScreen(screen: Screen) {
        when (screen) {
            Screen.GENERAL -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.general)
                }
            }
            Screen.BUSINESS -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.business)
                }
            }
            Screen.HEALTH -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.health)
                }
            }
            Screen.ENTERTAINMENT -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.entertainment)
                }
            }
            Screen.SCIENCE -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.science)
                }
            }
            Screen.SPORTS -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.sports)
                }
            }
            Screen.TECHNOLOGY -> {
                _articles.value = _articles.value?.also {
                    it.updateCategory(Category.technology)
                }
            }
        }

    }
    val articles = _articles.switchMap {
        when (it.category) {
            Category.general -> {
                println("is general here >>>>")
                receiveGeneralArticleData(it)
            }
            Category.business -> {
                println("is business here >>>>")
                receiveBusinessArticleData(it)
            }
            Category.entertainment -> {
                receiveEntertainmentArticleData(it)
            }
            Category.science -> {
                receiveScienceArticleData(it)
            }
            Category.technology -> {
                receiveTechnologyArticleData(it)
            }
            Category.health -> {
                receiveHealthArticleData(it)
            }
            Category.sports -> {
                receiveSportsArticleData(it)
            }
        }
    }

    private fun receiveGeneralArticleData(news: NewsType): LiveData<ArticleViewState> {
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

    private fun receiveBusinessArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.business.name,
            pageNo = news.businessArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.businessArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.businessArticleCache.articleList
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
                                articleState = ArticleState.Success(news.businessArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.businessArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.businessArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.businessArticleCache.articleList),
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

    private fun receiveEntertainmentArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.entertainment.name,
            pageNo = news.entertainmentArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.entertainmentArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.entertainmentArticleCache.articleList
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
                                articleState = ArticleState.Success(news.entertainmentArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.entertainmentArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.entertainmentArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.entertainmentArticleCache.articleList),
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

    private fun receiveScienceArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.science.name,
            pageNo = news.scienceArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.scienceArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.scienceArticleCache.articleList
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
                                articleState = ArticleState.Success(news.scienceArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.scienceArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.scienceArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.scienceArticleCache.articleList),
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

    private fun receiveTechnologyArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.technology.name,
            pageNo = news.technologyArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.technologyArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.technologyArticleCache.articleList
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
                                articleState = ArticleState.Success(news.technologyArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.technologyArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.technologyArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.technologyArticleCache.articleList),
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

    private fun receiveHealthArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.health.name,
            pageNo = news.healthArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.healthArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.healthArticleCache.articleList
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
                                articleState = ArticleState.Success(news.healthArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.healthArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.healthArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.healthArticleCache.articleList),
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

    private fun receiveSportsArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.sports.name,
            pageNo = news.sportsArticleCache.getOffset()
        ).switchMap { data: Resource<List<NewsArticle>> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.sportsArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.sportsArticleCache.articleList
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
                                articleState = ArticleState.Success(news.sportsArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.sportsArticleCache.updateInitialOffset()
                        for (i in data.data!!) {
                            news.sportsArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.sportsArticleCache.articleList),
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
