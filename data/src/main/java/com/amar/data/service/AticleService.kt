package com.amar.data.service

import com.amar.data.common.ConstantConfig.API_EVERYTHING
import com.amar.data.common.ConstantConfig.API_TOP_HEADLINE
import com.amar.data.entities.NewsArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ArticleService {

    @GET(API_TOP_HEADLINE)
    suspend fun getTopHeadLine(@Header("Authorization")key: String, @QueryMap options: Map<String, String>): Response<NewsArticleResponse>

    @GET(API_EVERYTHING)
    suspend fun getEverything(@Header("Authorization")key: String, @QueryMap options: Map<String, String>): NewsArticleResponse

}