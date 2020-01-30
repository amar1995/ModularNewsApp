package com.amar.data.entities

import com.google.gson.annotations.SerializedName

data class NewsArticleResponse(
    @SerializedName("status")
    val status: String = "fail",

    @SerializedName("totalResults")
    val totalResults: Int? = 0,

    @SerializedName("articles")
    val article: List<NewsArticle>
)