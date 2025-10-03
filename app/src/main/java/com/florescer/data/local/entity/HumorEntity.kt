package com.florescer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "humor_entries")
data class HumorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mood: String,
    val symptoms: String,
    val heartRate: String,
    val comment: String,
    val timestamp: Long
)