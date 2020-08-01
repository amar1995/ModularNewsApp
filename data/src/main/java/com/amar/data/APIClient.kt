package com.amar.data

import com.amar.data.common.AuthInterceptor
import com.amar.data.common.ConstantConfig.BASE_URL
import com.amar.data.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.NEWS_API_KEY))
            .build()
        inline fun <reified T> retrofitServiceProvider(): T {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(T::class.java)
        }
    }
}