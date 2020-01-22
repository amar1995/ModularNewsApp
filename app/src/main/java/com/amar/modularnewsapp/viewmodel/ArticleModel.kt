package com.amar.modularnewsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.common.ViewState
import com.amar.modularnewsapp.repository.ArticleRepo

class ArticleModel(articleRepo: ArticleRepo): ViewModel() {

    private var nationHeadline : LiveData<ViewState> = articleRepo.loadHeadlineNation().asLiveData()

    fun content() = nationHeadline
}