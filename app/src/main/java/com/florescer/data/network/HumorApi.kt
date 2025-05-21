package com.florescer.data.network

import com.florescer.data.model.Humor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HumorApi {
    @GET("humor")
    suspend fun getHumores(): List<Humor>

    @POST("humor")
    suspend fun postHumor(@Body humor: Humor): Response<Unit>
}
