package com.florescer.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        tokenProvider()?.let { token ->
            if (token.isNotBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
                requestBuilder.addHeader("X-User-Id", "1234")

                println("🔐 [AUTH] Adicionando Bearer Token: ${token.take(10)}...")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}