package com.amar.data.worker

import androidx.compose.Context
import androidx.compose.onCommit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.lifecycle.Observer
import androidx.work.Worker
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.entities.NewsArticle
import com.amar.data.repository.ArticleRepo
import com.amar.data.service.ArticleService
import com.amar.data.vo.Resource
import com.amar.data.vo.Status
import kotlinx.coroutines.*

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        doRefreshWork()
        return Result.success()
    }

    private suspend fun doRefreshWork() {
        val database = DatabaseClient.getInstance(applicationContext)
        val server = APIClient.retrofitServiceProvider<ArticleService>()
        val articleRepo = ArticleRepo.getInstance(database, server, applicationContext)
        database.articleDao().deleteArticles()
        withContext(Dispatchers.Main) {
            val data = articleRepo.refreshData(1)
            data.observeForever(Observer<Resource<List<NewsArticle>>> {
            })
        }
    }
}