package com.amar.data.service

import androidx.lifecycle.LiveData
import com.amar.data.common.ConstantConfig.API_EVERYTHING
import com.amar.data.common.ConstantConfig.API_TOP_HEADLINE
import com.amar.data.entities.NewsArticleResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ArticleService {

    // https://newsapi.org/v2/everything?q=sports&language=en

    // &country=us&country=jp&country=au&country=ca&country=za&country=sg&country=mx&country=hk

    @GET(API_TOP_HEADLINE)
    fun getArticle(@QueryMap options: Map<String, String>): LiveData<ApiResponse<NewsArticleResponse?>>

    @GET(API_EVERYTHING)
    fun getEverything(@QueryMap options: Map<String, String>): LiveData<ApiResponse<NewsArticleResponse?>>

}