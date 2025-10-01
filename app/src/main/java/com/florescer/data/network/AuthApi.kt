package com.florescer.data.network

import retrofit2.Response
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/token")
    suspend fun generateToken(): Response<String>
}
