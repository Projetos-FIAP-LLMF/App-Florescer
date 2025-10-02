package com.florescer.data.network

import com.florescer.data.TokenEntity
import retrofit2.Response
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/token")
    suspend fun generateToken(): Response<String>

    @POST("auth/registerDevice")
    suspend fun registerDevice(entity: TokenEntity): Response<Unit>
}
