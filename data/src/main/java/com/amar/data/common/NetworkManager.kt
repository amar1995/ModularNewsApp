package com.amar.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.amar.data.entities.NewsArticleResponse
import com.amar.data.service.*
import com.amar.data.vo.Resource
import com.amar.data.vo.Status
import kotlinx.coroutines.*
import retrofit2.Response


abstract class NetworkManager<ResultType, RequestType> {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        if (isOnlineRequest()) {
            // when have to make online request to
            val dbSource = AbsentLiveData.create<ResultType>()
            fetchFromNetwork(dbSource)
        } else {
            @Suppress("LeakingThis")
            val dbSource = loadFromDb()
            result.addSource(dbSource) { data ->
                result.removeSource(dbSource)
                if (shouldFetch(data) && isInternetAvailable()) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.success(data = newData))
                    }
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
        var apiResponse: LiveData<ApiResponse<RequestType>> = createCall()
        result.addSource(dbSource) {
            setValue(Resource.loading(it))
        }
        result.addSource(apiResponse) { response: ApiResponse<RequestType> ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            saveCallResult(processResponse(response))
                        }
                    }
                    if (isOnlineRequest()) {
                        result.addSource(AbsentLiveData.createWithResource(response.body)) {
                            setValue(Resource.success(data = it as ResultType))
                        }
                    } else {
                        result.addSource(loadFromDb()) {
                            setValue(Resource.success(data = it))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    result.addSource(loadFromDb()) { newData ->
                        setValue(Resource.success(data = newData))
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) {
                        setValue(Resource.error(response.errorMessage, it))
                    }
                }
                is UnAuthorizedResponse -> {
                    // Back to login screen
                    result.addSource(dbSource) {
                        setValue((Resource.unauthorized()))
                    }
                }
            }
        }
    }

    // retry after internet is back
    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body
    //save the network result into database
    abstract suspend fun saveCallResult(item: RequestType?)

    // check if internet available or is no data available in db
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun isInternetAvailable(): Boolean

    protected abstract fun isOnlineRequest(): Boolean

    // load from database if having db
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // making a network call
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}