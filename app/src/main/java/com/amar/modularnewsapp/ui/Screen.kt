package com.amar.modularnewsapp.ui

import androidx.compose.Model

sealed class Screen {
    object TopHeadLine: Screen()
    object Business: Screen()
    object Entertainment: Screen()
    object General: Screen()
    object Health: Screen()
    object Science: Screen()
    object Sports: Screen()
    object Technology: Screen()
}


@Model
object ScreenStatus {
    var currentScreen: Screen = Screen.TopHeadLine
}

fun navigateTo(destination: Screen) {
    ScreenStatus.currentScreen = destination
}
