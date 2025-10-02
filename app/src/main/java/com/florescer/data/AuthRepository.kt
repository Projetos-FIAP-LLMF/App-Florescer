package com.florescer.data

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


    suspend fun getUserId(): String = tokenMutex.withLock {

        cachedToken?.let {
            return it
        }


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

            val response = api.registerDevice(TokenEntity(
                id = 1,
                token = deviceId,
                createdAt = System.currentTimeMillis()
            ))

            val successful = response.isSuccessful
            successful
        } catch (e: Exception) {
            false
        }
    }
}