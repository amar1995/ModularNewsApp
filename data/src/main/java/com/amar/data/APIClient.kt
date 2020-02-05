package com.amar.data

import com.amar.data.common.AuthInterceptor
import com.amar.data.common.ConstantConfig.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {

    companion object {
        private val token = "8aefceae6e4e4e2c8ea0364cdf8b5aad"
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()
        inline fun <reified T> retrofitServiceProvider(): T {
            println("Is adapter called")
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(T::class.java)
        }
    }
}