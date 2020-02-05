package com.amar.data.repository

import com.amar.data.service.ArticleService
import com.amar.data.common.BaseDataSource

class ArticleRemoteDataSource(private val service: ArticleService, private val options: Map<String, String>): BaseDataSource() {

    suspend fun fetchData() = getResult{ service.getArticle(options) }

}