package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.compose.key
import androidx.compose.onCommit
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.Screen
import com.amar.modularnewsapp.ui.common.Observe
import com.amar.modularnewsapp.ui.common.ShowLoading
import com.amar.modularnewsapp.ui.common.SwipeToRefreshLayout
import com.amar.modularnewsapp.ui.util.NavigationStack
import com.amar.modularnewsapp.ui.util.slideRollLeftTransition
import com.amar.modularnewsapp.ui.util.slideRollRightTransition
import com.amar.modularnewsapp.viewmodel.ArticleState
import com.amar.modularnewsapp.viewmodel.ArticleViewState

@Composable
fun ShowArticleView(
    modifier: Modifier = Modifier,
    navigationStack: NavigationStack<MainScreen>,
    articles: ArticleViewState,
    endOfPage: () -> Unit = {},
    onRefresh: (Screen) -> Unit = {}
) {
    val articleList = articles.articleState as ArticleState.Success
    val scrollState = rememberScrollState()
    Observe {
        val isScrolledToEnd: Boolean = scrollState.value > scrollState.maxValue - 10
        onCommit(isScrolledToEnd) {
            if (isScrolledToEnd) {
                scrollState.scrollTo(scrollState.value)
                endOfPage()
            }
        }
    }
    SwipeToRefreshLayout(
        refreshingState = false,
        onRefresh = {
            // TODO implement refresh
//            when (navigationStack.current()) {
//                is MainScreen.General -> {
//                    onRefresh(Screen.GENERAL)
//                }
//                is MainScreen.Business -> {
//                    onRefresh(Screen.BUSINESS)
//                }
//                is MainScreen.Technology -> {
//                    onRefresh(Screen.TECHNOLOGY)
//                }
//                is MainScreen.Science -> {
//                    onRefresh(Screen.SCIENCE)
//                }
//                is MainScreen.Health -> {
//                    onRefresh(Screen.HEALTH)
//                }
//                is MainScreen.Sports -> {
//                    onRefresh(Screen.SPORTS)
//                }
//                is MainScreen.Entertainment -> {
//                    onRefresh(Screen.ENTERTAINMENT)
//                }
//            }
        },
        refreshIndicator = {
            Surface(elevation = 10.dp, shape = CircleShape) {
                CircularProgressIndicator(
                    modifier = Modifier.preferredSize(48.dp).padding(
                        4.dp
                    ),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    ) {
        ScrollableColumn(scrollState = scrollState, modifier = modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (navigationStack.current()) {
                    is MainScreen.General -> {
                        showText(value = Screen.GENERAL.name)
                    }
                    is MainScreen.Business -> {
                        showText(value = Screen.BUSINESS.name)
                    }
                    is MainScreen.Technology -> {
                        showText(value = Screen.TECHNOLOGY.name)
                    }
                    is MainScreen.Science -> {
                        showText(value = Screen.SCIENCE.name)
                    }
                    is MainScreen.Health -> {
                        showText(value = Screen.HEALTH.name)
                    }
                    is MainScreen.Sports -> {
                        showText(value = Screen.SPORTS.name)
                    }
                    is MainScreen.Entertainment -> {
                        showText(value = Screen.ENTERTAINMENT.name)
                    }
                }
                articleList.articles.forEach {
                    key(it.key) {
                        ShowArticle(article = it.value, onClick = {
                            navigationStack.next(
                                next = MainScreen.Detail_view(it.value),
                                enterTransition = slideRollLeftTransition,
                                exitTransition = slideRollRightTransition
                            )
                        })
                    }
                }
                when {
                    articles.isLoadingMorePage -> LoadingMoreArticleView()
                    !articles.hasLoadedAllPages -> LoadMoreArticleButton(
                        onPageEndReached = endOfPage
                    )
                    articles.hasLoadedAllPages -> NoMoreArticleView()
                }
            }
        }
    }
}

@Composable
private fun showText(value: String) {
    Text(
        text = value,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
        style = MaterialTheme.typography.body1.copy(
            MaterialTheme.colors.onBackground.copy(
                0.6f
            )
        )
    )
}

@Composable
private fun ShowArticle(
    article: NewsArticle,
    onClick: () -> Unit
) {
    ArticleTicket(
        article = article,
        modifier = Modifier.clickable(onClick = onClick)
    )
}


@Composable
private fun LoadingMoreArticleView() {
    Box(
        padding = 8.dp,
        gravity = ContentGravity.Center,
        modifier = androidx.ui.core.Modifier.fillMaxWidth()
    ) {
        ShowLoading()
    }
}

@Composable
private fun LoadMoreArticleButton(
    onPageEndReached: () -> Unit
) {
    Box(
        gravity = ContentGravity.Center,
        modifier = androidx.ui.core.Modifier.fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Button(onClick = onPageEndReached) {
            Text(
                "Load more",
                style = MaterialTheme.typography.subtitle2,
                modifier = androidx.ui.core.Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 8.dp
                )
            )
        }
    }
}

@Composable
private fun NoMoreArticleView() {
    Box(
        gravity = ContentGravity.Center,
        modifier = androidx.ui.core.Modifier.fillMaxWidth()
    ) {
        Text(
            text = "No more news...",
            style = MaterialTheme.typography.subtitle2
                .copy(
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                ),
            modifier = androidx.ui.core.Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            )
        )
    }
}
