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
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Close
import androidx.ui.material.icons.filled.Menu
import androidx.ui.text.AnnotatedString
import androidx.ui.text.SpanStyle
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.ui.article.ShowArticleView
import com.amar.modularnewsapp.ui.common.AppBarType
import com.amar.modularnewsapp.ui.common.ShowLoading
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
import java.text.SimpleDateFormat
import java.util.*

private lateinit var newArticleModel: ArticleModel

class MainActivity : AppCompatActivity() {
    private lateinit var navigationStack: NavigationStack<MainScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newArticleModel =
            ViewModelProviders.of(
                this,
                BaseViewModelFactory { ArticleModel(application = this.application) })
                .get(ArticleModel::class.java)

        println("Activty A onCreate")
        setContent {
            navigationStack = remember { NavigationStack(MainScreen.General) }

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
}

@Composable
fun NewsApp(navigationStack: NavigationStack<MainScreen>) {
    Route(navigationStack.getTransition()) { screen: MainScreen ->
        when (screen) {
            is MainScreen.Search -> {
                SearchScreen(
                    articleModel = newArticleModel,
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
    val context = ContextAmbient.current
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
                newArticleModel.updateScreen(Screen.BUSINESS)
            }
            MainScreen.General -> {
                newArticleModel.updateScreen(Screen.GENERAL)
            }
            MainScreen.Technology -> {
                newArticleModel.updateScreen(Screen.TECHNOLOGY)
            }
            MainScreen.Entertainment -> {
                newArticleModel.updateScreen(Screen.ENTERTAINMENT)
            }
            MainScreen.Sports -> {
                newArticleModel.updateScreen(Screen.SPORTS)
            }
            MainScreen.Science -> {
                newArticleModel.updateScreen(Screen.SCIENCE)
            }
            MainScreen.Health -> {
                newArticleModel.updateScreen(Screen.HEALTH)
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
    val internationalState by newArticleModel.articles.observeAsState()
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxWidth()) {
        if (internationalState == null) {
            ShowLoading()
        } else {
            when (internationalState!!.articleState) {
                is ArticleState.Loading -> {
                    ShowLoading()
                }
                is ArticleState.Success -> {
                    ShowArticleView(
                        navigationStack = navigationStack,
                        articles = internationalState!!,
                        endOfPage = {
                            newArticleModel.endOfPage(Screen.GENERAL)
                        }
                    )
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
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                IconButton(onClick = {
                    navigationStack.back()
                }, modifier = Modifier) {
                    Icon(asset = Icons.Filled.Close, tint = MaterialTheme.colors.onSurface)
                }
                Spacer(modifier = Modifier.height(16.dp))
                article.urlToImage?.let { imageUrl ->
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                                + Modifier.preferredHeight(164.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        UrlImage(url = imageUrl, width = 100.dp, height = 100.dp)
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
//            onClick()
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

