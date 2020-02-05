package com.amar.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.amar.data.vo.Result
import com.amar.data.vo.Status
import kotlinx.coroutines.Dispatchers

fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                          networkCall: suspend () -> Result<A>,
                          saveCallResult: suspend (A) -> Unit,
                          deleteData: suspend () -> Unit = {},
                          shouldFetch: Boolean = false): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading(null))
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)
        if(shouldFetch) {
            val responseStatus = networkCall.invoke()
            println("Data : " + responseStatus.data)
            if (responseStatus.status == Status.SUCCESS) {
//                deleteData()
                saveCallResult(responseStatus.data!!)
            } else if (responseStatus.status == Status.ERROR) {
                emit(Result.error<T>(responseStatus.message!!, null))
                emitSource(source)
            }
        }
    }