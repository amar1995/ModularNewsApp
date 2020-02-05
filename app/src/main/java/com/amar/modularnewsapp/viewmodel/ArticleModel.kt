package com.amar.modularnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.data.repository.ArticleRepo
import com.amar.data.repository.PageSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class ArticleModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = SupervisorJob()
    private var viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)
//    private var _dataResource = MutableLiveData<Resource<List<NewsArticle>>>()
//
//    val data: MutableLiveData<Resource<List<NewsArticle>>>
//    get() = _dataResource
    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val articleRepo: ArticleRepo by lazy {
        ArticleRepo.getInstance(
            DatabaseClient.getInstance(application),
            APIClient.retrofitServiceProvider<ArticleService>(),
            application.applicationContext
        )
    }
    val articleData = articleRepo.articleData
    init {
        refreshData()
    }

    val internationalHeadline: LiveData<List<NewsArticle>> = articleRepo.internationalHeadline

    fun getCategoryInternationalHeadline(category: String): LiveData<List<NewsArticle>> {
        return articleRepo.getCategoryInternationalHeadline(category)
    }

    fun loadMoreData() {
        viewModelScope.launch {
            println("New data size " + PageSize.topHeadlineInternationalPageNo)
            articleRepo.loadMoreData(++PageSize.topHeadlineInternationalPageNo)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                articleRepo.refreshData()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                if (internationalHeadline.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}