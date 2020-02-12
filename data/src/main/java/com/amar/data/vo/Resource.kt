package com.amar.data.vo

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(status: Status = Status.SUCCESS, data: T?, message: String? = null): Resource<T> {
            return Resource(status, data, message)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> unauthorized(): Resource<T> {
            return Resource(Status.UNAUTHORIZED, null, null)
        }
    }
}