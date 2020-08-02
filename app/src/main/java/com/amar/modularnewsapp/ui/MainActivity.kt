package com.amar.modularnewsapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollableColumn
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Close
import androidx.ui.material.icons.filled.FilterList
import androidx.ui.material.icons.filled.Menu
import androidx.ui.material.icons.filled.Refresh
import androidx.ui.text.AnnotatedString
import androidx.ui.text.SpanStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.ui.article.ShowArticleView
import com.amar.modularnewsapp.ui.common.AppBarType
import com.amar.modularnewsapp.ui.common.ShowLoading
import com.amar.modularnewsapp.ui.common.SwipeToRefreshLayout
import com.amar.modularnewsapp.ui.common.UrlImage
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.ui.search.SearchScreen
import com.amar.modularnewsapp.ui.theme.DistillTheme
import com.amar.modularnewsapp.ui.util.NavigationStack
import com.amar.modularnewsapp.ui.util.Route
import com.amar.modularnewsapp.ui.util.fadeInTransition
import com.amar.modularnewsapp.ui.util.fadeOutTransition
import com.amar.modularnewsapp.viewmodel.ArticleModel
import com.amar.modularnewsapp.viewmodel.ArticleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

private lateinit var articleModel: ArticleModel

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var navigationStack: NavigationStack<MainScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleModel =
            ViewModelProviders.of(
                this,
                BaseViewModelFactory { ArticleModel(application = this.application) })
                .get(ArticleModel::class.java)
        navigationStack = NavigationStack(init = MainScreen.General)
        println("Activty A onCreate")
        setContent {

            DistillTheme {
                NewsApp(navigationStack)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("Activity A onStart")
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

    override fun onBackPressed() {
        if (navigationStack.back() == null) {
            super.onBackPressed()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
}

@Composable
fun NewsApp(navigationStack: NavigationStack<MainScreen>) {
    Route(navigationStack.getTransition()) { screen: MainScreen ->
        when (screen) {
            is MainScreen.Search -> {
                SearchScreen(
                    articleModel = articleModel,
                    navigationStack = navigationStack
                )
            }
            is MainScreen.Detail_view -> {
                showNewArticleInDetail(article = screen.article, navigationStack = navigationStack)
            }
            else -> {
                AppContent(navigationStack = navigationStack)
            }
        }
    }
}


@Composable
fun AppContent(
    navigationStack: NavigationStack<MainScreen>
) {
    val scaffoldState = ScaffoldState(isDrawerGesturesEnabled = false)
    Scaffold(
        topBar = {
            AppBarType(
                title = null,
                navigationIcon = {
                    IconButton(onClick = { scaffoldState.drawerState = DrawerState.Opened }) {
                        Icon(Icons.Filled.Menu)
                    }
                },
                actions = {
                    // TODO remove indication
                    articleModel.clearSearch()
                    Text(
                        text = "Search News",
                        modifier = Modifier.weight(1f) + Modifier.clickable(
                            onClick = {
                                navigationStack.next(
                                    next = MainScreen.Search,
                                    enterTransition = fadeInTransition,
                                    exitTransition = fadeOutTransition
                                )
                            }),
                        style = MaterialTheme.typography.body1
                    )
                    IconButton(onClick = {
                        when (navigationStack.current()) {
                            is MainScreen.General -> {
                                articleModel.refresh(Screen.GENERAL)
                            }
                            is MainScreen.Business -> {
                                articleModel.refresh(Screen.BUSINESS)
                            }
                            is MainScreen.Technology -> {
                                articleModel.refresh(Screen.TECHNOLOGY)
                            }
                            is MainScreen.Science -> {
                                articleModel.refresh(Screen.SCIENCE)
                            }
                            is MainScreen.Health -> {
                                articleModel.refresh(Screen.HEALTH)
                            }
                            is MainScreen.Sports -> {
                                articleModel.refresh(Screen.SPORTS)
                            }
                            is MainScreen.Entertainment -> {
                                articleModel.refresh(Screen.ENTERTAINMENT)
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Refresh)
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            NavigationDrawer(
                onDrawerStateChange = { scaffoldState.drawerState = it },
                backgroundColor = (MaterialTheme.colors).surface,
                navigationStack = navigationStack
            )
        }
    ) {
        when (navigationStack.current()) {
            MainScreen.Business -> {
                articleModel.updateScreen(Screen.BUSINESS)
            }
            MainScreen.General -> {
                articleModel.updateScreen(Screen.GENERAL)
            }
            MainScreen.Technology -> {
                articleModel.updateScreen(Screen.TECHNOLOGY)
            }
            MainScreen.Entertainment -> {
                articleModel.updateScreen(Screen.ENTERTAINMENT)
            }
            MainScreen.Sports -> {
                articleModel.updateScreen(Screen.SPORTS)
            }
            MainScreen.Science -> {
                articleModel.updateScreen(Screen.SCIENCE)
            }
            MainScreen.Health -> {
                articleModel.updateScreen(Screen.HEALTH)
            }
        }
        Surface(color = (MaterialTheme.colors).surface) {
            Column {
                CustomTab(navigationStack = navigationStack)
            }
        }
    }
}


@Composable
fun CustomTab(
    navigationStack: NavigationStack<MainScreen>
) {
    val articleState by articleModel.articles.observeAsState()
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxWidth()) {
        if (articleState == null) {
            ShowLoading()
        } else {
            when (articleState!!.articleState) {
                is ArticleState.Loading -> {
                    ShowLoading()
                }
                is ArticleState.Success -> {
//                    SwipeToRefreshLayout(
//                        refreshingState = false,
//                        onRefresh = {
//                            // TODO implement refresh
//                            when (navigationStack.current()) {
//                                is MainScreen.General -> {
//                                    articleModel.refresh(Screen.GENERAL)
//                                }
//                                is MainScreen.Business -> {
//                                    articleModel.refresh(Screen.BUSINESS)
//                                }
//                                is MainScreen.Technology -> {
//                                    articleModel.refresh(Screen.TECHNOLOGY)
//                                }
//                                is MainScreen.Science -> {
//                                    articleModel.refresh(Screen.SCIENCE)
//                                }
//                                is MainScreen.Health -> {
//                                    articleModel.refresh(Screen.HEALTH)
//                                }
//                                is MainScreen.Sports -> {
//                                    articleModel.refresh(Screen.SPORTS)
//                                }
//                                is MainScreen.Entertainment -> {
//                                    articleModel.refresh(Screen.ENTERTAINMENT)
//                                }
//                            }
//                        },
//                        refreshIndicator = {
//                            Surface(elevation = 10.dp, shape = CircleShape) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.preferredSize(48.dp).padding(
//                                        4.dp
//                                    ),
//                                    color = MaterialTheme.colors.onSurface
//                                )
//                            }
//                        }
//                    ) {
                        ShowArticleView(
                            navigationStack = navigationStack,
                            articles = articleState!!,
                            endOfPage = {
                                when (navigationStack.current()) {
                                    MainScreen.Business -> {
                                        articleModel.endOfPage(Screen.BUSINESS)
                                    }
                                    MainScreen.General -> {
                                        articleModel.endOfPage(Screen.GENERAL)
                                    }
                                    MainScreen.Technology -> {
                                        articleModel.endOfPage(Screen.TECHNOLOGY)
                                    }
                                    MainScreen.Entertainment -> {
                                        articleModel.endOfPage(Screen.ENTERTAINMENT)
                                    }
                                    MainScreen.Sports -> {
                                        articleModel.endOfPage(Screen.SPORTS)
                                    }
                                    MainScreen.Science -> {
                                        articleModel.endOfPage(Screen.SCIENCE)
                                    }
                                    MainScreen.Health -> {
                                        articleModel.endOfPage(Screen.HEALTH)
                                    }
                                }
                            }
                        )
//                    }
                }
                is ArticleState.Error -> {
                    // error view
                }
            }
        }
    }
}

@Composable
private fun showNewArticleInDetail(
    article: NewsArticle?,
    navigationStack: NavigationStack<MainScreen>
) {
    val context = ContextAmbient.current
    val typography = MaterialTheme.typography
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        if (article != null) {
            ScrollableColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                IconButton(onClick = {
                    navigationStack.back()
                }, modifier = Modifier) {
                    Icon(asset = Icons.Filled.Close, tint = MaterialTheme.colors.onSurface)
                }
                Spacer(modifier = Modifier.height(16.dp))
                article.urlToImage?.let { imageUrl ->
                    if(!imageUrl.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth()
                                    + Modifier.preferredHeight(164.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            UrlImage(url = imageUrl, width = 100.dp, height = 100.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                val emphasisLevels = EmphasisAmbient.current
                ProvideEmphasis(emphasisLevels.high) {
                    Text(
                        text = if (article.title == null) "Title not given" else article.title!!,
                        style = typography.h6.copy(color = (MaterialTheme.colors).onSurface)
                    )
                }
                ProvideEmphasis(emphasisLevels.high) {
                    Text(
                        text = if (article.author == null) "Unknow author" else article.author!!,
                        style = typography.body2.copy(color = (MaterialTheme.colors).onSurface)
                    )
                }
                ProvideEmphasis(emphasisLevels.medium) {
                    Text(
                        text = dateToString(article.publishedTime!!),
                        style = typography.body2.copy(color = (MaterialTheme.colors).onSurface)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (article.description == null) "" else article.description!!,
                    style = typography.body1.copy(color = (MaterialTheme.colors).onSurface)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if (article.content == null) "" else article.content!!,
                    style = typography.body2.copy(
                        color = (MaterialTheme.colors).onSurface.copy(
                            alpha = 0.7f
                        )
                    )
                )
                Text(
                    modifier = Modifier.padding(8.dp).clickable(onClick = {
                        if (article.url == null) {
                            Toast.makeText(
                                context,
                                "Unable to show",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(article.url)
                                )
                            )
                        }
                    }),
                    text = AnnotatedString(
                        text = "see more...",
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )

            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("Unknow Error !!!!")
            }
        }
    }
}

fun dateToString(value: Date): String {
    val ft = SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz")
    return ft.format(value)
}

@Composable
fun <T> observer(data: LiveData<T>): T? {
    var result = state<T?> { data.value }
    val observer = remember { Observer<T> { result.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }
    return result.value
}

