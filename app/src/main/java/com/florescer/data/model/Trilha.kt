package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class Trilha(
    val id: String,
    @SerializedName("title")
    val nome: String,
    @SerializedName("description")
    val descricao: String? = null
)
