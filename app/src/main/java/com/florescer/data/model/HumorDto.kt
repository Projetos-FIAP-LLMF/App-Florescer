package com.florescer.data.model

data class HumorDTO(
    val mood: String,
    val note: String,
    val physicalSymptoms: List<String>,
    val heartRate: Int?
)
