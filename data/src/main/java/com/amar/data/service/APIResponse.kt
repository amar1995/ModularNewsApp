package com.amar.data.service

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused")
sealed class APIResponse<T> {
    fun <T> create(error: Throwable): ApiErrorResponse<T> {
        return ApiErrorResponse(error.message ?: "unknown error")
    }

    fun <T> create(response: Response<T>): APIResponse<T> =
        if (response.isSuccessful) {
            val body = response.body()
            if (body == null || response.code() == 204) {
                ApiEmptyResponse()
            } else if (response.code() == 401) {
                UnAuthorizedResponse()
            } else {
                ApiSuccessResponse(
                    body = body
                )
            }
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

class ApiEmptyResponse<T> : APIResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : APIResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : APIResponse<T>()

class UnAuthorizedResponse<T> : APIResponse<T>()
