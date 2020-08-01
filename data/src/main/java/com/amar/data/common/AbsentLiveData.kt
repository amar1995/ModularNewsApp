package com.amar.data.common

import androidx.lifecycle.LiveData

class AbsentLiveData<T : Any?> private constructor(data: T?): LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(data)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData(null)
        }
        fun <T> createWithResource(data: T): LiveData<T> {
            return AbsentLiveData(data)
        }
    }
}