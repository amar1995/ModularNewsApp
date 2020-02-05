package com.amar.data.service

import com.amar.data.common.ConstantConfig.API_EVERYTHING
import com.amar.data.common.ConstantConfig.API_TOP_HEADLINE
import com.amar.data.entities.NewsArticleResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ArticleService {
    // &country=us&country=jp&country=au&country=ca&country=za&country=sg&country=mx&country=hk
    @GET(API_TOP_HEADLINE + "?country=in")
    suspend fun getTopHeadLine(@QueryMap options: Map<String, String>): NewsArticleResponse

    @GET(API_TOP_HEADLINE + "?country=in")
    suspend fun getArticle(@QueryMap options: Map<String, String>): Response<NewsArticleResponse>

    @GET(API_EVERYTHING)
    fun getEverything(@Header("Authorization") key: String, @QueryMap options: Map<String, String>): Call<NewsArticleResponse>

}