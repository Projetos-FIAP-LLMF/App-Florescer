package com.florescer.data

import com.florescer.data.model.*
import com.florescer.data.network.*
import java.lang.Exception
import com.florescer.data.network.HumorApi

class HumorRepository(
    private val humorApi: HumorApi,
    private val afirmacoesApi: AfirmacoesApi,
    private val analiseSintomasApi: AnaliseSintomasApi,
    private val recomendacoesApi: RecomendacoesApi,
    private val evolucaoApi: EvolucaoApi,
    private val recursosApi: RecursosApi,
    private val trilhasApi: TrilhasApi,
    private val videosApi: VideosApi,
    private val sonsApi: SonsApi,
    private val dao: HumorDao
) {

    // -------------------------
    // HUMORES LOCAIS
    // -------------------------
    suspend fun saveHumorLocal(humor: HumorEntry) = dao.insert(humor)
    suspend fun getHumoresLocais(): List<HumorEntry> = dao.getAll()

    // -------------------------
    // ENVIO PARA BACKEND (/checkin)
    // -------------------------
    suspend fun sendHumorToBackend(humor: HumorEntry, userId: String) {
        val symptomsList = if (humor.symptoms.isBlank()) emptyList()
        else humor.symptoms.split(",").map { it.trim() }

        val dto = HumorDTO(
            mood = humor.mood.uppercase(),
            note = humor.comment,
            physicalSymptoms = symptomsList,
            heartRate = humor.heartRate.toIntOrNull()
        )

        val response = humorApi.postHumor(userId, dto)
        if (response.mood.isBlank()) {
            throw Exception("Erro ao enviar check-in")
        }
    }

    // -------------------------
    // HUMORES DO BACKEND
    // -------------------------
    suspend fun getHumores(): List<Humor> = humorApi.getHumores()

    // -------------------------
    // EVOLUÇÃO
    // -------------------------
    suspend fun getHistoricoEvolucao(
        userId: String,
        dateFrom: String? = null,
        dateTo: String? = null,
        limit: Int? = null
    ): List<EvolucaoHistorico> =
        evolucaoApi.getHistoricos(userId, dateFrom, dateTo, limit)

    // -------------------------
    // RECURSOS
    // -------------------------
    suspend fun getRecursos(userId: String, limit: Int? = 10): List<Recurso> =
        recursosApi.getRecursos(userId, limit)

    suspend fun getTrilhas(userId: String, limit: Int? = 10): List<Trilha> =
        trilhasApi.getTrilhas(userId, limit)

    suspend fun getVideos(userId: String, limit: Int? = 10): List<Video> =
        videosApi.getVideos(userId, limit)

    suspend fun getSons(userId: String, limit: Int? = 10): List<Som> =
        sonsApi.getSons(userId, limit)

    // -------------------------
    // AFIRMAÇÕES
    // -------------------------
    suspend fun getAfirmacoesPorHumor(
        userId: String,
        mood: String,
        limit: Int? = 3
    ): List<Afirmacao> {
        val all = afirmacoesApi.getAfirmacoesPorHumor(userId, limit, mood)
        return all.firstOrNull()?.affirmations ?: emptyList()
    }


    // -------------------------
    // RECOMENDAÇÕES
    // -------------------------
    suspend fun getRecomendacoes(userId: String, limit: Int? = 5): List<Recomendacao> =
        recomendacoesApi.get(userId, limit)

    // -------------------------
    // POST SINTOMAS / ANALISE
    // -------------------------
    suspend fun postSintomas(userId: String, sintomas: SintomasEntry): List<Recomendacao> =
        analiseSintomasApi.postSintomas(userId, sintomas).filter { it.descricao != null }
}
