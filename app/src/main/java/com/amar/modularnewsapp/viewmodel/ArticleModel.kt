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
import com.amar.data.entities.NewsArticleResponse
import com.amar.data.repository.ArticleRepo
import com.amar.data.vo.Resource
import com.amar.data.vo.Status
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.Screen
import com.amar.modularnewsapp.ui.util.NavigationStack
import kotlinx.coroutines.*

class ArticleCache {
    // id, data
    var articleList: LinkedHashMap<String, NewsArticle> = LinkedHashMap()
    private var offset: Int = 0
    private var initialOffset: Int = 0
    private var refresh: Boolean = false

    fun init() {
        this.offset = 0
        this.articleList = linkedMapOf()
        this.initialOffset = 0
    }

    fun refresh() {
        this.offset = 0
        this.initialOffset = 0
        this.refresh = true
        this.articleList.clear()
    }

    fun refreshDone() {
        this.refresh = false
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

    fun isRefreshing(): Boolean {
        return this.refresh
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

object SearchNews {
    var value = ""
    var searchArticleCache = ArticleCache()
    fun clear() {
        this.value = ""
        this.searchArticleCache.refresh()
    }

    fun query(value: String) {
        this.value = value
        this.searchArticleCache.refresh()
    }
}

class ArticleModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = SupervisorJob()
    private var viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val articleRepo: ArticleRepo by lazy {
        ArticleRepo.getInstance(
            DatabaseClient.getInstance(application),
            APIClient.retrofitServiceProvider(),
            application.applicationContext
        )
    }
    private var _articles: MutableLiveData<NewsType> = MutableLiveData()
    private var _searches: MutableLiveData<SearchNews> = MutableLiveData()
    val search: LiveData<SearchNews> = _searches
    val navigationStack: NavigationStack<MainScreen> = NavigationStack(init = MainScreen.General)

    init {
        _articles.postValue(NewsType)
        _searches.postValue(SearchNews)
    }

    fun endOfPage(screen: Screen) {
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

    fun endOfSearchPage() {
        _searches.value = _searches.value?.also {
            it.searchArticleCache.updateOffset()
        }
    }

    fun refresh(screen: Screen) {
        when (screen) {
            Screen.GENERAL -> {
                _articles.value = _articles.value?.also {
                    it.generalArticleCache.refresh()
                }
            }
            Screen.BUSINESS -> {
                _articles.value = _articles.value?.also {
                    it.businessArticleCache.refresh()
                }
            }
            Screen.HEALTH -> {
                _articles.value = _articles.value?.also {
                    it.healthArticleCache.refresh()
                }
            }
            Screen.ENTERTAINMENT -> {
                _articles.value = _articles.value?.also {
                    it.entertainmentArticleCache.refresh()
                }
            }
            Screen.SCIENCE -> {
                _articles.value = _articles.value?.also {
                    it.scienceArticleCache.refresh()
                }
            }
            Screen.SPORTS -> {
                _articles.value = _articles.value?.also {
                    it.sportsArticleCache.refresh()
                }
            }
            Screen.TECHNOLOGY -> {
                _articles.value = _articles.value?.also {
                    it.technologyArticleCache.refresh()
                }
            }
        }
    }

    private fun clearRefresh(screen: Screen) {
        when (screen) {
            Screen.GENERAL -> {
                _articles.value = _articles.value?.also {
                    it.generalArticleCache.refreshDone()
                }
            }
            Screen.BUSINESS -> {
                _articles.value = _articles.value?.also {
                    it.businessArticleCache.refreshDone()
                }
            }
            Screen.HEALTH -> {
                _articles.value = _articles.value?.also {
                    it.healthArticleCache.refreshDone()
                }
            }
            Screen.ENTERTAINMENT -> {
                _articles.value = _articles.value?.also {
                    it.entertainmentArticleCache.refreshDone()
                }
            }
            Screen.SCIENCE -> {
                _articles.value = _articles.value?.also {
                    it.scienceArticleCache.refreshDone()
                }
            }
            Screen.SPORTS -> {
                _articles.value = _articles.value?.also {
                    it.sportsArticleCache.refreshDone()
                }
            }
            Screen.TECHNOLOGY -> {
                _articles.value = _articles.value?.also {
                    it.technologyArticleCache.refreshDone()
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

    fun search(query: String) {
        if (!query.isNullOrBlank()) {
            _searches.value = _searches.value.also {
                it!!.query(query)
            }
        }
    }

    fun clearSearch() {
        SearchNews.clear()
        _searches.postValue(SearchNews)
    }

    val articles = _articles.switchMap {
        when (it.category) {
            Category.general -> {
                if(it.generalArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.general.name)
                        clearRefresh(Screen.GENERAL)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveGeneralArticleData(it)
                }
            }
            Category.business -> {
                if(it.businessArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.business.name)
                        clearRefresh(Screen.BUSINESS)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveBusinessArticleData(it)
                }
            }
            Category.entertainment -> {
                if(it.entertainmentArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.entertainment.name)
                        clearRefresh(Screen.ENTERTAINMENT)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveEntertainmentArticleData(it)
                }
            }
            Category.science -> {
                if(it.scienceArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.science.name)
                        clearRefresh(Screen.SCIENCE)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveScienceArticleData(it)
                }
            }
            Category.technology -> {
                if(it.technologyArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.technology.name)
                        clearRefresh(Screen.TECHNOLOGY)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveTechnologyArticleData(it)
                }
            }
            Category.health -> {
                if(it.healthArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.health.name)
                        clearRefresh(Screen.HEALTH)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveHealthArticleData(it)
                }
            }
            Category.sports -> {
                if(it.sportsArticleCache.isRefreshing()) {
                    viewModelScope.launch {
                        articleRepo.deleteArticlesByCategory(Category.sports.name)
                        clearRefresh(Screen.SPORTS)
                    }
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = ArticleState.Loading,
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                } else {
                    receiveSportsArticleData(it)
                }
            }
        }
    }

    val searches = _searches.switchMap {
        receiveSearchArticleData(it)
    }

    private fun receiveGeneralArticleData(news: NewsType): LiveData<ArticleViewState> {
        return articleRepo.loadData(
            category = Category.general.name,
            pageNo = news.generalArticleCache.getOffset() + 1
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
            pageNo = news.businessArticleCache.getOffset() + 1
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
            pageNo = news.entertainmentArticleCache.getOffset() + 1
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
            pageNo = news.scienceArticleCache.getOffset() + 1
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
            pageNo = news.technologyArticleCache.getOffset() + 1
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
            pageNo = news.healthArticleCache.getOffset() + 1
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
            pageNo = news.sportsArticleCache.getOffset() + 1
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

    private fun receiveSearchArticleData(news: SearchNews): LiveData<ArticleViewState> {
        return articleRepo.searchData(
            query = news.value,
            pageNo = news.searchArticleCache.getOffset() + 1
        ).switchMap { data: Resource<NewsArticleResponse?> ->
            when (data.status) {
                Status.LOADING -> {
                    AbsentLiveData.createWithResource(
                        ArticleViewState(
                            articleState = if (news.searchArticleCache.articleList.isNullOrEmpty()) ArticleState.Loading else ArticleState.Success(
                                news.searchArticleCache.articleList
                            ),
                            isLoadingMorePage = true,
                            hasLoadedAllPages = false
                        )
                    )
                }
                Status.SUCCESS -> {
                    if (data.data == null) {
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.searchArticleCache.articleList),
                                isLoadingMorePage = false,
                                hasLoadedAllPages = true
                            )
                        )
                    } else {
                        news.searchArticleCache.updateInitialOffset()
                        for (i in data.data!!.article) {
                            news.searchArticleCache.articleList.put(i.url, i)
                        }
                        AbsentLiveData.createWithResource(
                            ArticleViewState(
                                articleState = ArticleState.Success(news.searchArticleCache.articleList),
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
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
