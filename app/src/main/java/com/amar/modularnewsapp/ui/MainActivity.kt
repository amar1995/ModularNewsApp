package com.amar.modularnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.material.Button
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.service.ArticleService
import com.amar.modularnewsapp.common.BaseViewModelFactory
import com.amar.modularnewsapp.repository.ArticleRepo
import com.amar.modularnewsapp.ui.common.TopAppBar
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
            MaterialTheme {
                val (drawerState, onStateChange) = +state { DrawerState.Closed }
                Column() {
                    TopAppBar(
                        onDrawerStateChange = onStateChange,
                        backgroundColor = Color.Gray,
                        onSearchClick = {}
                    )
                    Greeting("Android")
                    Button(text = "Second", onClick = {

                    })
                }
            }
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
