package com.dicoding.githubusers1.data

import com.dicoding.githubusers1.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val okhttp = OkHttpClient.Builder()
        .apply {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(400, TimeUnit.SECONDS)
        .connectTimeout(70, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okhttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val listUserService = retrofit.create<ListUserService>()
}