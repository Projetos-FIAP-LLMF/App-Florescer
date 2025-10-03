package com.florescer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florescer.data.local.entity.HumorEntity

@Dao
interface HumorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HumorEntity)

    @Query("SELECT * FROM humor_entries ORDER BY timestamp DESC")
    suspend fun getAll(): List<HumorEntity>

    @Delete
    suspend fun delete(entry: HumorEntity)
}