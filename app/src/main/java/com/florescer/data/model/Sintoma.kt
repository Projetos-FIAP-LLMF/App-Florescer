package com.florescer.data.model

data class SintomasEntry(
    val sintomas: List<String>
)

data class SintomasResponse(
    val sintomas: List<Recomendacao>
)