package com.amar.data.common

object ConstantConfig {
    const val TABLE_NAME: String = "articles"
    const val DATABASE_NAME: String = "news"

    const val VERSION: Int = 1

    const val BASE_URL: String = "https://newsapi.org"
    const val API_VERSION: String = "/v2"

    const val API_TOP_HEADLINE: String = API_VERSION + "/top-headlines"

    const val API_EVERYTHING: String = API_VERSION + "/everything"
}