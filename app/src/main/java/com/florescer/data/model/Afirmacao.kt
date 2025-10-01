package com.florescer.data.model

data class AfirmacoesPorHumor(
    val id: String,
    val mood: String,
    val affirmations: List<Afirmacao>
)

data class Afirmacao(
    val text: String
)
