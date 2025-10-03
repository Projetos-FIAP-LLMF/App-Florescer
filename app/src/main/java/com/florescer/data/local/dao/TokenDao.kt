package com.florescer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florescer.data.local.entity.TokenEntity

@Dao
interface TokenDao {
    @Query("SELECT * FROM tokens WHERE id = 1 LIMIT 1")
    suspend fun getToken(): TokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: TokenEntity)

    @Query("DELETE FROM tokens")
    suspend fun clearToken()
}