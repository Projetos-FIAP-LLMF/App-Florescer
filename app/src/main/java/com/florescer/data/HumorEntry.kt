package com.florescer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "humor_entries")
data class HumorEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mood: String,
    val symptoms: String,
    val heartRate: String,
    val comment: String,
    val timestamp: Long
)