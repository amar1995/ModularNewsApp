package com.amar.data.service

import retrofit2.Response

/**
 * Common class used by Api responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused")
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> =
            if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body = body
                    )
                }
            } else {
                println("Is response Made >>>> " + response.code())
                if (response.code() == 401) {
                    UnAuthorizedResponse()
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    ApiErrorResponse(errorMsg ?: "unknown error")
                }
            }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

class UnAuthorizedResponse<T> : ApiResponse<T>()
