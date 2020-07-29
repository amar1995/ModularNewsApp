package com.amar.modularnewsapp.ui


enum class Screen {
    TOP_HEADLINE,
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
    object Top_HeadLine : MainScreen(Screen.TOP_HEADLINE)
    object General : MainScreen(Screen.GENERAL)
    object Business : MainScreen(Screen.BUSINESS)
    object Sports : MainScreen(Screen.SPORTS)
    object Entertainment : MainScreen(Screen.ENTERTAINMENT)
    object Health : MainScreen(Screen.HEALTH)
    object Science : MainScreen(Screen.SCIENCE)
    object Technology : MainScreen(Screen.TECHNOLOGY)
    object Search : MainScreen(Screen.SEARCH)
    object Detail_view : MainScreen(Screen.DETAIL_VIEW)
    object Setting : MainScreen(Screen.SETTING)
}
