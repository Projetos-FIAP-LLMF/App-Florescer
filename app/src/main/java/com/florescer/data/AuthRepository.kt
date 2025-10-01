package com.florescer.data

import com.florescer.data.network.AuthApi
import com.florescer.data.TokenDao


class AuthRepository(private val  api: AuthApi,
                     private val tokenDao: TokenDao,
        private val authApi: AuthApi,
) {

    suspend fun generateAndSaveToken(): Result<String> {
        return try {
            val response = api.generateToken()
            if (response.isSuccessful) {
                val token = response.body() ?: ""
                saveToken(token)
                Result.success(token)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveToken(token: String) {

    }

    suspend fun hasToken(): Boolean {
        return false
    }

    suspend fun getStoredToken(): String? {
        return null
    }
}
