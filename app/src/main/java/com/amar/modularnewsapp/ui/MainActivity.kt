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
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.text.AnnotatedString
import androidx.ui.text.SpanStyle
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import com.amar.data.common.InternetConnection
import com.amar.data.entities.NewsArticle
import com.amar.data.repository.PageSize
import com.amar.data.vo.Status
import com.amar.data.worker.RefreshDataWorker
import com.amar.modularnewsapp.R
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.ui.article.ArticleTicket
import com.amar.modularnewsapp.ui.common.AppBarType
import com.amar.modularnewsapp.ui.common.UrlImage
import com.amar.modularnewsapp.ui.navigationBar.NavigationDrawer
import com.amar.modularnewsapp.ui.navigationBar.VectorImage
import com.amar.modularnewsapp.ui.theme.DistillTheme
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
            DistillTheme {
                MainScreen()
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
}

@Composable
fun MainScreen() {
    val context = ContextAmbient.current
    val (drawerState, onStateChange) = state { DrawerState.Closed }
    val (bottomDrawerState, bottomOnStateChange) = state { BottomDrawerState.Closed }
    val (detailArticleState: NewsArticle?, onDetailArticleChange: (NewsArticle?) -> Unit) = state<NewsArticle?> { null }
    val scaffoldState = ScaffoldState(isDrawerGesturesEnabled = false)
    Scaffold(
        topBar = {
            AppBarType(
                title = null,
                navigationIcon = {

                },
                actions = {

                }
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            NavigationDrawer(
                onDrawerStateChange = { scaffoldState.drawerState = it },
                backgroundColor = (MaterialTheme.colors).surface,
                context = context
            )
        }
    ) {
        BottomDrawerLayout(
            drawerState = bottomDrawerState,
            onStateChange = {},
            gesturesEnabled = true,
            drawerContent = {
                showNewArticleInDetail(
                    article = detailArticleState,
                    onClick = {
                        bottomOnStateChange(BottomDrawerState.Closed)
                        onDetailArticleChange(null)
                    }
                )
            }) {
            AppContent(
                onStateChange = onStateChange,
                bottomOnStateChange = bottomOnStateChange,
                onDetailArticleChange = onDetailArticleChange
            )
        }
    }
}


@Composable
fun AppContent(
    onStateChange: (DrawerState) -> Unit,
    bottomOnStateChange: (BottomDrawerState) -> Unit,
    onDetailArticleChange: (NewsArticle?) -> Unit
) {
    Surface(color = (MaterialTheme.colors).surface) {
        Column {
            CustomTab(bottomOnStateChange, onDetailArticleChange)
        }
    }
}


@Composable
fun CustomTab(
    bottomOnStateChange: (BottomDrawerState) -> Unit,
    onDetailArticleChange: (NewsArticle?) -> Unit
) {
    var internationalState = observer(newArticleModel.articleData2)
    var page = observer(newArticleModel.pageNo)
    Surface(color = (MaterialTheme.colors).surface, modifier = Modifier.fillMaxWidth()) {
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
                                if (scrollerPosition.isAtEndOfList)
                                    newArticleModel.updatePageNo(++PageSize.topHeadlineInternationalPageNo)
                            }
                        }
                        ScrollableColumn() {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                data.forEach {
                                    key(it) {
                                        ShowArticle(article = it, onClick = {
                                            onDetailArticleChange(it)
                                            bottomOnStateChange(BottomDrawerState.Opened)
                                        })
                                    }
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
                    Box() {
                        Text("Please add the news.org token")
                    }
                }
            }
        }
    }
}

@Composable
private fun Observe(body: @Composable() () -> Unit) = body

@Composable
private fun ShowArticle(
    article: NewsArticle,
    onClick: () -> Unit
) {
    ArticleTicket(
        backgroundColor = (MaterialTheme.colors).background,
        article = article,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun noContentMore(modifier: Modifier = Modifier) {
    Text("No more to show....")
}

@Composable
private fun showLoading(
    color: Color = MaterialTheme.colors.secondaryVariant,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), gravity = ContentGravity.Center) {
        CircularProgressIndicator(
            color = color,
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun showError(msg: String) {
    Text(
        msg,
        style = TextStyle(Color.Red)
    )
}

@Composable
private fun showNewArticleInDetail(
    article: NewsArticle?,
    onClick: () -> Unit
) {
    val context = ContextAmbient.current
    val typography = MaterialTheme.typography
    Surface(
        color = MaterialTheme.colors.surface,
        modifier = Modifier.padding(16.dp)
    ) {
        if (article != null) {
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(4.dp).clickable(onClick = onClick)
                ) {
                    VectorImage(
                        id = R.drawable.ic_baseline_close_24,
                        tint = (MaterialTheme.colors).onSurface
                    )
                }
                Spacer(Modifier.height(8.dp))
                article.urlToImage?.let { imageUrl ->
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.small)
                            .preferredHeight(100.dp)
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
                        text = article.publishedTime!!,
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

