package com.florescer.data.model

import com.google.gson.annotations.SerializedName

data class Recomendacao(
    @SerializedName("description")
    val descricao: String? = null
)