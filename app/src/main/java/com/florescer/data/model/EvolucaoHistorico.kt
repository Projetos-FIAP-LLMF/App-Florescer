package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class EvolucaoHistorico(
    @SerializedName("date")
    val data: String,
    @SerializedName("mood")
    val humor: String
)