package com.sendiko.justdoit.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val BASE_URL = "https://benjamin4k.my.id/justdoit/api/"

    private fun provideOkHttpClient(tokenRefresher: TokenRefresher?= null) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                tokenRefresher?.let {
                    client.authenticator(it)
                }
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun refreshToken() : RefreshTokenApi {
        val server = BASE_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(server)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
        return retrofit.create(RefreshTokenApi::class.java)
    }

    fun getInstance(context : Context) : ApiService {
        val authenticator = TokenRefresher(context, refreshToken())
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(authenticator))
            .build()
        return retrofit.create(ApiService::class.java)
    }

}