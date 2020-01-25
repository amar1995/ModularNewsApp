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
import androidx.ui.foundation.ColoredRect
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
            APIClient.retrofitServiceProvider<ArticleService>()
        )
    }

    val newArticleModel: ArticleModel by lazy {
        ViewModelProviders.of(this, BaseViewModelFactory { ArticleModel(articleRepo) })
            .get(ArticleModel::class.java)
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
    modifier: Modifier = Modifier.None
) {
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
    val state = +state { 0 }
    val titles = listOf("Nation", "International")
//    TODO
//    val indicatorContainer = @Composable { tabPositions: List<TabRow.TabPosition> ->
//        TabRow.IndicatorContainer(tabPositions = tabPositions, selectedIndex = state.value) {
//            FancyIndicator(Color.White)
//        }
//    }
    FlexColumn(modifier = modifier) {
        inflexible {
            TabRow(items = titles, selectedIndex = state.value) { index, text ->
                FancyTab(
                    title = text,
                    onClick = { state.value = index },
                    selected = (index == state.value)
                )
            }
        }
        flexible(flex = 1f) {
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
}

@Composable
private fun FancyTab(title: String, onClick: () -> Unit, selected: Boolean) {
    MutuallyExclusiveSetItem(selected = selected, onClick = { onClick() }) {
        Container(height = 50.dp, padding = EdgeInsets(10.dp)) {
            Column(
                ExpandedHeight
            ) {
                val color = if (selected) Color.White else Color.Gray
                Padding(5.dp) {
                    Text(text = title, style = TextStyle(
                        color = color,
                        fontFamily = bodyFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ))
                }
            }
        }
    }
}

@Composable
private fun FancyIndicator(color: Color) {
    Padding(5.dp) {
        Container(expanded = true) {
            DrawBorder(RoundedCornerShape(5.dp), Border(color, 2.dp))
        }
    }
}