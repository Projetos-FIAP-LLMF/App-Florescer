package com.florescer.data

import com.florescer.data.network.AuthApi
import com.florescer.data.TokenDao


class AuthRepository(
    private val api: AuthApi,
    private val tokenDao: TokenDao
) {
    suspend fun getUserId(): String {
        val storedToken = tokenDao.getToken()
        return storedToken?.token ?: "default-user-id"
    }

    private fun saveToken(token: String) {

    }

    suspend fun hasToken(): Boolean {
        return false
    }

    suspend fun getStoredToken(): String? {
        // aqui você busca no banco ou retorna null/default
        val storedTokenEntity = tokenDao.getToken() // supondo que você tenha essa função
        return storedTokenEntity?.token
    }
}
