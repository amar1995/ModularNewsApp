package com.amar.modularnewsapp.ui

import com.amar.data.entities.NewsArticle


enum class Screen {
    GENERAL,
    BUSINESS,
    SPORTS,
    ENTERTAINMENT,
    HEALTH,
    SCIENCE,
    TECHNOLOGY,
    SEARCH,
    DETAIL_VIEW,
    SETTING
}
sealed class MainScreen(val id: Screen) {
    object General : MainScreen(Screen.GENERAL)
    object Business : MainScreen(Screen.BUSINESS)
    object Sports : MainScreen(Screen.SPORTS)
    object Entertainment : MainScreen(Screen.ENTERTAINMENT)
    object Health : MainScreen(Screen.HEALTH)
    object Science : MainScreen(Screen.SCIENCE)
    object Technology : MainScreen(Screen.TECHNOLOGY)
    object Search : MainScreen(Screen.SEARCH)
    data class Detail_view(val article: NewsArticle) : MainScreen(Screen.DETAIL_VIEW)
    object Setting : MainScreen(Screen.SETTING)
}
