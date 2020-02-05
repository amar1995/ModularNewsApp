package com.amar.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.amar.data.entities.NewsArticleResponse
import com.amar.data.service.*
import com.amar.data.vo.Resource
import kotlinx.coroutines.*
import retrofit2.Response

abstract class NetworkManager<ResultType, RequestType> {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        var apiResponse:LiveData<ApiResponse<RequestType>> = createCall()
        println("APi response >>>>>>>> " + apiResponse)
        result.addSource(dbSource) {
            setValue(Resource.loading(it))
        }

        result.addSource(apiResponse) { response: ApiResponse<RequestType> ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    runBlocking {
                        println("Data: >>>>>>> " + response.body)
                        withContext(Dispatchers.IO) {
                            saveCallResult(processResponse(response))
                        }
                    }
                    result.addSource(loadFromDb()) {
                        println(">>>>>>>> " + it)
                        setValue(Resource.success(it))
                    }
                }
                is ApiEmptyResponse -> {
                    result.addSource(loadFromDb()) { newData ->
                        setValue(Resource.success(newData))
                    }
                }
                is ApiErrorResponse -> {
                    println("Error >>>>")
                    onFetchFailed()
                    result.addSource(dbSource) {
                        setValue(Resource.error(response.errorMessage, it))
                    }
                }
                is UnAuthorizedResponse -> {
                    // Back to login screen
                    println("Unauthorized >>>>")
                    result.addSource(dbSource) {
                        setValue((Resource.unauthorized()))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body
    //save the network result into database
    abstract suspend fun saveCallResult(item: RequestType?)

    // check if internet available or is no data available in db
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // load from database
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // making a network call
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}