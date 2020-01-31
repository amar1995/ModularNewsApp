package com.amar.modularnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.compose.frames.commit
import androidx.compose.frames.inFrame
import androidx.compose.frames.open
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.ui.core.*
import androidx.ui.foundation.ColoredRect
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.selection.MutuallyExclusiveSetItem
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.border.DrawBorder
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Canvas
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.modularnewsapp.R
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.common.ViewState
import com.amar.modularnewsapp.repository.ArticleRepo
import com.amar.modularnewsapp.ui.article.ArticleTicket
import com.amar.modularnewsapp.ui.common.Image
import com.amar.modularnewsapp.ui.common.TopAppBar
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.viewmodel.ArticleModel

private lateinit var newArticleModel: ArticleModel
val ScrollerPosition.isAtEndOfList: Boolean get() = value >= maxPosition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newArticleModel =
            ViewModelProviders.of(this, BaseViewModelFactory { ArticleModel(application = this.application) })
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
    MaterialTheme(
        colors = darkThemeColors,
        typography = themeTypography
    ) {
        val (drawerState, onStateChange) = +state { DrawerState.Closed }
        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onStateChange,
            gesturesEnabled = false,
            drawerContent = {
                NavigationDrawer(
                    onDrawerStateChange = onStateChange,
                    backgroundColor = (+MaterialTheme.colors()).surface
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

    val context = +ambient(ContextAmbient)
    Column() {
        Surface(color = (+MaterialTheme.colors()).surface) {
            Column {
                TopAppBar(
                    onDrawerStateChange = onStateChange,
                    backgroundColor = (+MaterialTheme.colors()).background,
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
    var internationalState = +observer(newArticleModel.internationalHeadline)
    Surface(color = (+MaterialTheme.colors()).surface, modifier = Expanded) {
        if (internationalState == null) {
            ShowLoading()
        } else if (internationalState.isEmpty()) {
            NoContentMore()
        } else {
            val scrollerPosition: ScrollerPosition = +memo { ScrollerPosition(0f) }
            println("MainActivity data here : " + internationalState)

            Observe {
                +onCommit(scrollerPosition.isAtEndOfList) {
                    println("Is commit entered")
                    if(scrollerPosition.isAtEndOfList)
                        newArticleModel.loadMoreData()
                }
            }
            VerticalScroller(scrollerPosition = scrollerPosition) {
                Column(Expanded) {
                    internationalState!!.forEach {
                        ArticleTicket(
                            backgroundColor = (+MaterialTheme.colors()).background,
                            article = it
                        )
                    }
                }
            }

             // TODO Loading problem to solve
//            newArticleModel.loadMoreData()
        }
    }
}

@Composable
private fun ShowArticle(
    articleList: List<NewsArticle>,
    scrollerPosition: ScrollerPosition
) {
    VerticalScroller(scrollerPosition = scrollerPosition) {
        Column(Expanded) {
            articleList.forEach {
                ArticleTicket(
                    backgroundColor = (+MaterialTheme.colors()).background,
                    article = it
                )
            }
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
    color: Color = (+MaterialTheme.colors()).primary,
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

fun <T> observer(data: LiveData<T>) = effectOf<T?> {
    var result = +state<T?> { data.value }
    val observer = +memo { Observer<T> { result.value = it } }

    +onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }
    result.value
}
