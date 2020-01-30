package com.amar.modularnewsapp.common

import androidx.compose.Composable
import androidx.compose.memo
import androidx.compose.unaryPlus
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.Expanded
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.article.ArticleTicket

sealed class ViewState<T> {
//    @Composable
//    abstract fun BuildUI()

    class Loading: ViewState<Unit>()
//    {
//        @Composable
//        override fun BuildUI() {
//            Container(alignment = Alignment.Center) {
//                CircularProgressIndicator(color = (+MaterialTheme.colors()).primary)
//            }
//        }
//    }

    data class Error(val reason: String = ""): ViewState<Unit>()
//    {
//        @Composable
//        override fun BuildUI() {
//            Container(alignment = Alignment.Center) {
//                Text("Something went wrong. Please check your internet connection", style = TextStyle(Color.Red))
//            }
//        }
//    }

    data class Success<T>(val data: T): ViewState<T>()
//    {
//        @Composable
//        override fun BuildUI(
//        ) {
//            val scrollerPosition: ScrollerPosition = +memo { ScrollerPosition(0f) }
//            VerticalScroller(scrollerPosition = scrollerPosition) {
//                Column(Expanded) {
//                    data.forEach {
//                        ArticleTicket(
//                            backgroundColor = (+MaterialTheme.colors()).background,
//                            article = it
//                        )
//                    }
//                }
//            }
//        }
//    }

    companion object {
        fun<T> success(data: T): ViewState<T> = Success(data)

        fun error(reason: String): ViewState<Unit> = Error(reason)

        fun loading(): ViewState<Unit> = Loading()

    }
}