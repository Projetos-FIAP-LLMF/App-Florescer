package com.florescer.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florescer.data.HumorEntry

@Dao
interface HumorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HumorEntry)

    @Query("SELECT * FROM humor_entries ORDER BY timestamp DESC")
    suspend fun getAll(): List<HumorEntry>

    @Delete
    suspend fun delete(entry: HumorEntry)
}
