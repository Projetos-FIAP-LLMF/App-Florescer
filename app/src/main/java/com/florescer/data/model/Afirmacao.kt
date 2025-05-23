package com.florescer.data.model

data class AfirmacoesPorHumor(
    val mood: String,
    val afirmacoes: List<Afirmacao>
)

data class Afirmacao(
    val texto: String
)
