package com.florescer.data

import com.florescer.data.model.Afirmacao
import com.florescer.data.model.EvolucaoHistorico
import com.florescer.data.model.Humor
import com.florescer.data.model.HumorDTO
import com.florescer.data.model.Recomendacao
import com.florescer.data.model.SintomasEntry
import com.florescer.data.network.AfirmacoesApi
import com.florescer.data.network.AnaliseSintomasApi
import com.florescer.data.network.EvolucaoApi
import com.florescer.data.network.HumorApi
import com.florescer.data.network.RecomendacoesApi

class HumorRepository(
    private val humorApi: HumorApi,
    private val afirmacoesApi: AfirmacoesApi,
    private val analiseSintomasApi: AnaliseSintomasApi,
    private val recomendacoesApi: RecomendacoesApi,
    private val evolucaoApi: EvolucaoApi,
    private val dao: HumorDao
) {

    suspend fun getHumores(): List<Humor> = humorApi.getHumores()

    suspend fun saveHumorLocal(humor: HumorEntry) = dao.insert(humor)

    suspend fun getHumoresLocais(): List<HumorEntry> = dao.getAll()

    suspend fun sendHumorToBackend(humor: HumorEntry) {
        val dto = HumorDTO(
            mood = humor.mood,
            symptoms = humor.symptoms,
            heartRate = humor.heartRate,
            comment = humor.comment,
            timestamp = humor.timestamp
        )
        humorApi.postHumor(dto)
    }

    suspend fun getAfirmacoesPorHumor(mood: String): List<Afirmacao> {
        val all = afirmacoesApi.getAfirmacoesPorHumor()
        return all.find { it.mood.equals(mood, ignoreCase = true) }?.afirmacoes ?: emptyList()
    }

    suspend fun postSintomas(sintomas: SintomasEntry): List<Recomendacao> =
        analiseSintomasApi.postSintomas(sintomas)
            .filter { it.descricao != null }

    suspend fun getRecomendacoes(): List<Recomendacao> = recomendacoesApi.get()

    suspend fun getHistoricoEvolucao(): List<EvolucaoHistorico> = evolucaoApi.getHistoricos()
}