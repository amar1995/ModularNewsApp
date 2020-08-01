package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.compose.key
import androidx.compose.onCommit
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.common.Observe
import com.amar.modularnewsapp.ui.common.ShowLoading
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
    endOfPage: () -> Unit = {}
) {
    val articleList = articles.articleState as ArticleState.Success
    val scrollState = rememberScrollState()
    Observe {
        val isScrolledToEnd: Boolean = scrollState.value > scrollState.maxValue - 10
//        println("scrolled value >>>> ${scrollState.maxValue} ${scrollState.value}")
        onCommit(isScrolledToEnd) {
            if (isScrolledToEnd) {
                scrollState.scrollTo(scrollState.value)
                endOfPage()
            }
        }
    }
    ScrollableColumn(scrollState = scrollState, modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
