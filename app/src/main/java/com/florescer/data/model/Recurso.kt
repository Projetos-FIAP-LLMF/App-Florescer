package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class Recurso(
    val id: String,
    @SerializedName("title")
    val titulo: String? = null,
    @SerializedName("description")
    val descricao: String? = null,
    @SerializedName("type")
    val tipo: String? = null
)