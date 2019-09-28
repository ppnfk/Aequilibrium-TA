package com.vincentcho.transformer.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RestClient
 *
 * This class help create a Retrofit instance and handle some prerequisites.
 *
 * @param baseUrl url of the rest server
 * @constructor Creates an new Retrofit instance
 */
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
    /**
     *
     * create a Retrofit Instance of <T>
     *
     * @param apiInterfaceClass interface class of the retrofit service api
     * @return a retrofit instance of class apiInterfaceClass
     */
    fun <T> create(apiInterfaceClass: Class<T>): T {
        return mClient.create(apiInterfaceClass)
    }
}