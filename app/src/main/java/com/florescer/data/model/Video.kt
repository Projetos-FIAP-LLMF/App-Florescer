package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    val id: String,
    @SerializedName("title") val title: String,
    val url: String
)