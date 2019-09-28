package com.vincentcho.transformer.api

import com.vincentcho.transformer.vo.Transformer
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Path

interface TransformerService {

    @GET("allspark")
    fun allspark(): Call<String>

    @POST("transformers")
    fun createTransformers(@Header("Authorization") allspark: String, @Body transformerJson: RequestBody): Call<Transformer>

    @GET("transformers")
    fun getTransformers(@Header("Authorization") allspark: String): Call<TransformerResponse>

    @PUT("transformers")
    fun updateTransformers(@Header("Authorization") allspark: String, @Body transformer: RequestBody): Call<Transformer>
    
    @DELETE("transformers/{transformerId}")
    fun deleteTransformer(@Header("Authorization") allspark: String, @Path("transformerId") tId: String): Call<ResponseBody>
}