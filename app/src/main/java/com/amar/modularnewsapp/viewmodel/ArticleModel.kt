package com.amar.modularnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
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


    val articleRepo: ArticleRepo by lazy {
        ArticleRepo.getInstance(
            DatabaseClient.getInstance(application),
            APIClient.retrofitServiceProvider<ArticleService>(),
            application.applicationContext
        )
    }
    private val _pageNo: MutableLiveData<Int> = MutableLiveData<Int>()
    init {
        _pageNo.value = 1
    }
    val pageNo: LiveData<Int>
        get() = _pageNo

    val articleData2 = _pageNo.switchMap {  articleRepo.loadData(it) }
    fun updatePageNo(pageNo: Int) {
        if(_pageNo.value != pageNo)
            _pageNo.value = pageNo
    }
}