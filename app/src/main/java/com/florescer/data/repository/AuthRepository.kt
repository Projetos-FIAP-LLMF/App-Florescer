package com.florescer.data.repository

import com.florescer.data.local.dao.TokenDao
import com.florescer.data.local.entity.TokenEntity
import com.florescer.data.network.AuthApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class AuthRepository(
    private val api: AuthApi,
    private val tokenDao: TokenDao
) {
    private val tokenMutex = Mutex()
    private var cachedToken: String? = null
    private var cachedAuthToken: String? = null


    suspend fun getAuthToken(): String? {
        return cachedAuthToken
    }

    suspend fun setAuthToken(token: String) {
        cachedAuthToken = token
    }

    // ID do dispositivo (já existente)
    suspend fun getUserId(): String = tokenMutex.withLock {
        cachedToken?.let { return it }
        val storedToken = tokenDao.getToken()
        if (storedToken != null) {
            cachedToken = storedToken.token
            return storedToken.token
        }
        val newToken = generateDeviceId()
        saveTokenLocal(newToken)
        cachedToken = newToken
        return newToken
    }

    private fun generateDeviceId(): String {
        return "device_${UUID.randomUUID()}"
    }

    private suspend fun saveTokenLocal(token: String) {
        val tokenEntity = TokenEntity(
            id = 1,
            token = token,
            createdAt = System.currentTimeMillis()
        )
        tokenDao.insertToken(tokenEntity)
    }

    suspend fun hasToken(): Boolean {
        return tokenDao.getToken() != null
    }

    suspend fun getStoredToken(): String? {
        return tokenDao.getToken()?.token
    }

    suspend fun clearToken() {
        tokenMutex.withLock {
            tokenDao.clearToken()
            cachedToken = null
        }
    }

    suspend fun registerDeviceInBackend(): Boolean {
        return try {
            val deviceId = getUserId()

            val response = api.generateToken()

            if (response.isSuccessful) {
                val body = response.body()
                val token = body
                if (!token.isNullOrBlank()) {
                    setAuthToken(token)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

}