package com.amar.modularnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.*
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Canvas
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.Expanded
import androidx.ui.material.Button
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ModalDrawerLayout
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.modularnewsapp.R
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.repository.ArticleRepo
import com.amar.modularnewsapp.ui.article.ArticleTicket
import com.amar.modularnewsapp.ui.common.Image
import com.amar.modularnewsapp.ui.common.TopAppBar
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.viewmodel.ArticleModel

class MainActivity : AppCompatActivity() {
    val articleRepo: ArticleRepo by lazy {
        ArticleRepo.getInstance(
            DatabaseClient.getInstance(this.applicationContext).articleDao(),
            APIClient.retrofitServiceProvider<ArticleService>())
    }

    val newArticleModel: ArticleModel by lazy {
        ViewModelProviders.of(this, BaseViewModelFactory { ArticleModel(articleRepo) }).get(ArticleModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        newArticleModel.content().observe(this, Observer  { state ->
//            when(state) {
//                is ViewState.Success<*> -> { println("Data: " + state.data) }
//                is ViewState.Loading -> { println("Loading") }
//                is ViewState.Error -> { println( "Error: " + state.reason )}
//            }
//        })
        println("Activty A onCreate")

        setContent {
            MainScreen()
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
fun MainScreen() {
    MaterialTheme(
        colors = darkThemeColors,
        typography = themeTypography
    ) {
        val (drawerState, onStateChange) = +state { DrawerState.Closed }
//        AppContent(onStateChange = onStateChange)
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
                AppContent(onStateChange = onStateChange)
            }
        )
    }
}


@Composable
fun AppContent(onStateChange: (DrawerState) -> Unit) {
    println("Main color" + (+MaterialTheme.colors()).background.value)
    val articleSample = NewsArticle(
        source = null,
        author = "The times Of Rock",
        category = null,
        title = "Coronavirus Live Updates: Deaths Recorded Hundreds of Miles from Center of Outbreak - The New York Times",
        urlToImage = "https://static01.nyt.com/images/2020/01/24/world/24china-briefing-1/24china-briefing-1-facebookJumbo.jpg",
        publishedTime = "Now",
        content = "sfui sdf l",
        articleType = "",
        country = "",
        description = "",
        id = 1L,
        url = ""
    )
    val context = +ambient(ContextAmbient)
    Column() {
        Surface(color = (+MaterialTheme.colors()).surface) {
            TopAppBar(
                onDrawerStateChange = onStateChange,
                backgroundColor = (+MaterialTheme.colors()).background,
                onSearchClick = {}
            )
        }
        Surface(color = (+MaterialTheme.colors()).surface, modifier = Expanded) {
            VerticalScroller() {
                Column(Expanded) {

                    ArticleTicket(
                        backgroundColor = (+MaterialTheme.colors()).background,
                        article = articleSample
                    )
                    ArticleTicket(
                        backgroundColor = (+MaterialTheme.colors()).background,
                        article = articleSample
                    )
                    ArticleTicket(
                        backgroundColor = (+MaterialTheme.colors()).background,
                        article = articleSample
                    )
                    ArticleTicket(
                        backgroundColor = (+MaterialTheme.colors()).background,
                        article = articleSample
                    )
                }
            }
        }
    }
}