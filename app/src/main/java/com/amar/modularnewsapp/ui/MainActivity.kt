package com.amar.modularnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutSize
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ModalDrawerLayout
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.repository.PageSize
import com.amar.modularnewsapp.ui.article.ArticleTicket
import com.amar.modularnewsapp.ui.common.TopAppBar
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.viewmodel.ArticleModel

private lateinit var newArticleModel: ArticleModel
val ScrollerPosition.isAtEndOfList: Boolean get() = value >= maxPosition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newArticleModel =
            ViewModelProviders.of(
                this,
                BaseViewModelFactory { ArticleModel(application = this.application) })
                .get(ArticleModel::class.java)
        println("Activty A onCreate")

        setContent {
            MainScreen(this)
        }
    }

    override fun onStart() {
        super.onStart()
        println("Activty A onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Activty A onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Activty A onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Activty A onStop")
    }

    override fun onRestart() {
        super.onRestart()
        println("Activty A onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        // on Destroy clear database
    }
}

@Composable
fun MainScreen(activity: AppCompatActivity) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colors = if (dark) darkThemeColors else lightThemeColors,
        typography = themeTypography
    ) {
        val (drawerState, onStateChange) = state { DrawerState.Closed }

        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onStateChange,
            gesturesEnabled = false,
            drawerContent = {
                NavigationDrawer(
                    onDrawerStateChange = onStateChange,
                    backgroundColor = (MaterialTheme.colors()).surface
                )
            },
            bodyContent = {
                AppContent(onStateChange = onStateChange, activty = activity)
            }
        )
    }
}


@Composable
fun AppContent(onStateChange: (DrawerState) -> Unit, activty: AppCompatActivity) {
    Column() {
        Surface(color = (MaterialTheme.colors()).surface) {
            Column {
                TopAppBar(
                    onDrawerStateChange = onStateChange,
                    backgroundColor = (MaterialTheme.colors()).background,
                    onSearchClick = {}
                )
                CustomTab()
            }
        }

    }
}

@Composable
fun CustomTab(
) {

    var internationalState = observer(newArticleModel.internationalHeadline)
    Surface(color = (MaterialTheme.colors()).surface, modifier = LayoutSize.Fill) {
        if (internationalState == null) {
            ShowLoading()
        } else if (internationalState.isEmpty()) {
            NoContentMore()
        } else {
            val scrollerPosition: ScrollerPosition = ScrollerPosition(0f)
            println("MainActivity data here : " + internationalState)

            Observe {
                onCommit(scrollerPosition.isAtEndOfList) {
                    println("Is commit entered")
                    if (scrollerPosition.isAtEndOfList)
                        newArticleModel.loadMoreData()
                }
            }
            VerticalScroller(scrollerPosition = scrollerPosition) {
                Column(modifier = LayoutSize.Fill) {
                    println("Page rendering size " + PageSize.topHeadlineInternationalPageNo)
                    internationalState!!.forEach {
                        ShowArticle(article = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowArticle(
    @Pivotal article: NewsArticle
) {
    println(article)
    Ripple(bounded = true) {
        Clickable() {
            ArticleTicket(
                backgroundColor = (MaterialTheme.colors()).background,
                article = article
            )
        }
    }
}

@Composable
private fun NoContentMore(modifier: Modifier = Modifier.None) {
    Container(modifier = modifier) {
        Text("No more to show....")
    }
}

@Composable
private fun ShowLoading(
    color: Color = (MaterialTheme.colors()).primary,
    modifier: Modifier = Modifier.None
) {
    Container(alignment = Alignment.Center, modifier = modifier) {
        Text("Loading ..... ")
    }
}

@Composable
private fun ShowError() {
    Container(alignment = Alignment.Center) {
        Text(
            "Something went wrong. Please check your internet connection",
            style = TextStyle(Color.Red)
        )
    }
}

fun <T> observer(data: LiveData<T>): T? {
    var result = state<T?> { data.value }
    val observer = remember { Observer<T> { result.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }
    return result.value
}

