package com.vincentcho.transformer.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.vincentcho.transformer.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*



class RestClient(baseUrl: String) {
    private val mClient: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY


        val okClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }.addInterceptor(interceptor).build()

        var gson = GsonBuilder()
            .setLenient()
            .create()

        mClient = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> create(apiInterfaceClass: Class<T>): T {
        return mClient.create(apiInterfaceClass)
    }
}