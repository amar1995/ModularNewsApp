package com.amar.modularnewsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.entities.NewsArticle
import com.amar.data.service.ArticleService
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.common.ViewState
import com.amar.modularnewsapp.repository.ArticleRepo
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
        newArticleModel.content().observe(this, Observer  { state ->
            when(state) {
                is ViewState.Success<*> -> { println("Data: " + state.data) }
                is ViewState.Loading -> { println("Loading") }
                is ViewState.Error -> { println( "Error: " + state.reason )}
            }
        })
        setContent {
            MaterialTheme {
                Greeting("Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}
