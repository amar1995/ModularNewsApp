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
import androidx.ui.core.*
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.text.AnnotatedString
import androidx.ui.text.SpanStyle
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.amar.data.common.InternetConnection
import com.amar.data.entities.NewsArticle
import com.amar.data.repository.PageSize
import com.amar.data.vo.Status
import com.amar.data.worker.RefreshDataWorker
import com.amar.modularnewsapp.R
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.ui.article.ArticleTicket
import com.amar.modularnewsapp.ui.common.Image
import com.amar.modularnewsapp.ui.common.TopAppBar
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.ui.navigationBar.VectorImage
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
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).build()
        val dataRefreshWork = OneTimeWorkRequestBuilder<RefreshDataWorker>()
            .setConstraints(constraints).build()
//        --------- uncomment to refresh on app start
//        WorkManager.getInstance(applicationContext).enqueue(dataRefreshWork)
        setContent {
            MainScreen(this)
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
}

@Composable
fun MainScreen(activity: AppCompatActivity) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colors = if (dark) darkThemeColors else lightThemeColors,
        typography = themeTypography
    ) {
        val (drawerState, onStateChange) = state { DrawerState.Closed }
        val (bottomDrawerState, bottomOnStateChange) = state { DrawerState.Closed }
        val (detailArticleState: NewsArticle?, onDetailArticleChange: (NewsArticle?) -> Unit) = state<NewsArticle?> { null }
        BottomDrawerLayout(drawerState = bottomDrawerState,
            onStateChange = {},
            gesturesEnabled = true,
            drawerContent = {
                //            Text("Hello to news lover")
                showNewArticleInDetail(
                    article = detailArticleState,
                    onClick = {
                        bottomOnStateChange(DrawerState.Closed)
                        onDetailArticleChange(null)
                    },
                    activty = activity)
            }){
            ModalDrawerLayout(
                drawerState = drawerState,
                onStateChange = onStateChange,
                gesturesEnabled = false,
                drawerContent = {
                    NavigationDrawer(
                        onDrawerStateChange = onStateChange,
                        backgroundColor = (MaterialTheme.colors()).surface,
                        context = activity.applicationContext
                    )
                },
                bodyContent = {
                    AppContent(
                        onStateChange = onStateChange,
                        bottomOnStateChange = bottomOnStateChange,
                        onDetailArticleChange = onDetailArticleChange,
                        activty = activity
                    )
                }
            )
        }
    }
}


@Composable
fun AppContent(
    onStateChange: (DrawerState) -> Unit,
    bottomOnStateChange: (DrawerState) -> Unit,
    onDetailArticleChange: (NewsArticle?) -> Unit,
    activty: AppCompatActivity
) {
    Column() {
        Surface(color = (MaterialTheme.colors()).surface) {
            Column {
                TopAppBar(
                    onDrawerStateChange = onStateChange,
                    backgroundColor = (MaterialTheme.colors()).background,
                    onSearchClick = {
                        Toast.makeText(activty.applicationContext, "TBD...", Toast.LENGTH_SHORT).show()
                    }
                )
                CustomTab(bottomOnStateChange, onDetailArticleChange)
            }
        }

    }
}


@Composable
fun CustomTab(
    bottomOnStateChange: (DrawerState) -> Unit,
    onDetailArticleChange: (NewsArticle?) -> Unit
) {
    var internationalState = observer(newArticleModel.articleData2)
    var page = observer(newArticleModel.pageNo)
    Surface(color = (MaterialTheme.colors()).surface, modifier = LayoutSize.Fill) {
        if (internationalState == null) {
            showLoading()
        } else {
            when (internationalState!!.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    val data = internationalState.data
                    if (data.isNullOrEmpty()) {
                        noContentMore()
                    } else {
                        val scrollerPosition: ScrollerPosition = ScrollerPosition(0f)

                        Observe {
                            onCommit(scrollerPosition.isAtEndOfList) {
                                println("Is commit entered")
                                if (scrollerPosition.isAtEndOfList)
                                    newArticleModel.updatePageNo(++PageSize.topHeadlineInternationalPageNo)
                            }
                        }
                        VerticalScroller(scrollerPosition = scrollerPosition) {
                            Column(modifier = LayoutSize.Fill) {
                                data.forEach {
                                    ShowArticle(article = it, onClick = {
                                        onDetailArticleChange(it)
                                        bottomOnStateChange(DrawerState.Opened)
                                    })
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    val context = ContextAmbient.current
                    if (!InternetConnection.isAvailable(context = context)) {
                        showError(msg = "No Internet")
                    } else {
                        showError(msg = if (internationalState.message == null) "Unknow Error!!!" else internationalState.message!!)
                    }
                }
                Status.UNAUTHORIZED -> {
                    Container(alignment = Alignment.Center) {
                        Text("Please add the news.org token")
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowArticle(
    @Pivotal article: NewsArticle,
    onClick: () -> Unit
) {
    Ripple(bounded = true) {
        Clickable(onClick = onClick) {
            ArticleTicket(
                backgroundColor = (MaterialTheme.colors()).background,
                article = article
            )
        }
    }
}

@Composable
private fun noContentMore(modifier: Modifier = Modifier.None) {
    Container(modifier = modifier) {
        Text("No more to show....")
    }
}

@Composable
private fun showLoading(
    color: Color = (MaterialTheme.colors()).primary,
    modifier: Modifier = Modifier.None
) {
    Container(alignment = Alignment.Center, modifier = modifier) {
        Text("Loading ..... ")
    }
}

@Composable
private fun showError(msg: String) {
    Container(alignment = Alignment.Center) {
        Text(
            msg,
            style = TextStyle(Color.Red)
        )
    }
}

@Composable
private fun showNewArticleInDetail(article: NewsArticle?, onClick: () -> Unit, activty: AppCompatActivity) {
    val typography = MaterialTheme.typography()
    Surface(
        color = (MaterialTheme.colors()).surface,
        modifier = LayoutPadding(16.dp)
    ) {
        if (article != null) {
            Column(modifier = LayoutSize.Fill + LayoutPadding(8.dp)) {
                Clickable(onClick) {
                    Container(
                        LayoutWidth.Fill + LayoutPadding(4.dp),
                        alignment = Alignment.TopStart
                    ) {
                        VectorImage(
                            id = R.drawable.ic_baseline_close_24,
                            tint = (MaterialTheme.colors()).onSurface
                        )
                    }
                }
                Spacer(LayoutHeight(8.dp))
                article.urlToImage?.let { imageUrl ->
                    Container(modifier = LayoutWidth.Fill, height = 200.dp) {
                        Clip(shape = RoundedCornerShape(4.dp)) {
                            Image(url = imageUrl, width = 100.dp, height = 200.dp)
                        }
                    }
                }
                Spacer(LayoutSize(16.dp))
                val emphasisLevels = MaterialTheme.emphasisLevels()
                ProvideEmphasis(emphasisLevels.high) {
                    Text(
                        text = if (article.title == null) "Title not given" else article.title!!,
                        style = typography.h6.copy(color = (MaterialTheme.colors()).onSurface)
                    )
                }
                ProvideEmphasis(emphasisLevels.high) {
                    Text(
                        text = if (article.author == null) "Unknow author" else article.author!!,
                        style = typography.body2.copy(color = (MaterialTheme.colors()).onSurface)
                    )
                }
                ProvideEmphasis(emphasisLevels.medium) {
                    Text(
                        text = article.publishedTime!!,
                        style = typography.body2.copy(color = (MaterialTheme.colors()).onSurface)
                    )
                }
                Spacer(LayoutHeight(10.dp))
                Text(
                    text = if(article.description == null ) "" else article.description!!,
                    style = typography.body1.copy(color = (MaterialTheme.colors()).onSurface)
                )
                Spacer(LayoutHeight(20.dp))

                Text(
                    modifier = LayoutPadding(8.dp),
                    text = if(article.content == null) "" else article.content!!,
                    style = typography.body2.copy(color = (MaterialTheme.colors()).onSurface.copy(alpha = 0.7f))
                )
                Clickable(onClick = {
                    if(article.url == null) {
                        Toast.makeText(activty.applicationContext, "Unable to show", Toast.LENGTH_SHORT).show()
                    } else {
                        activty.startActivity(Intent(Intent.ACTION_VIEW , Uri.parse(article.url)))
                    }
                }) {
                    Text(
                        modifier = LayoutPadding(8.dp),
                        text = AnnotatedString(
                            text = "see more...",
                            spanStyle = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    )
                }
            }
        } else {
            Column(LayoutSize.Fill) {
                Text("Unknow Error !!!!")
            }
            onClick()
        }
    }
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

