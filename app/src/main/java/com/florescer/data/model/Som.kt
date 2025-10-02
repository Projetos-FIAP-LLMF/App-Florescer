package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class Som(
    val id: String,
    @SerializedName("title") val nome: String,
    val url: String
)