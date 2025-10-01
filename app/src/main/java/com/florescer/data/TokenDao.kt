package com.florescer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florescer.data.TokenEntity

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: TokenEntity)

    @Query("SELECT * FROM token_table WHERE id = 0 LIMIT 1")
    suspend fun getToken(): TokenEntity?

    @Query("DELETE FROM token_table")
    suspend fun deleteAll()
}