package com.amar.modularnewsapp.common

sealed class ViewState {
    class Loading: ViewState()

    data class Error(val reason: String = ""): ViewState()

    data class Success<T>(val data: T): ViewState()

    companion object {
        fun<T> success(data: T): ViewState = Success(data)

        fun error(reason: String): ViewState = Error(reason)

        fun loading(): ViewState = Loading()

    }
}