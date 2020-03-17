package com.amar.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amar.data.APIClient
import com.amar.data.DatabaseClient
import com.amar.data.repository.ArticleRepo
import com.amar.data.service.ArticleService

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
//        withContext(Dispatchers.Main) {
//            val data = articleRepo.refreshData(1)
//            data.observeForever(Observer<Resource<List<NewsArticle>>> {
//            })
//        }
    }
}