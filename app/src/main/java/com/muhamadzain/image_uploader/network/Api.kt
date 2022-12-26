package com.muhamadzain.image_uploader.network

import com.muhamadzain.image_uploader.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    val endpoint : ApiInterface
        get() {
            val interceptor= HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
}