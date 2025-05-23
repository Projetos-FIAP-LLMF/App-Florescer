package com.florescer.data.network

import com.florescer.data.model.HumorDTO
import com.florescer.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface HumorApi {
    @GET("humor")
    suspend fun getHumores(): List<Humor>

    @POST("checkins")
    suspend fun postHumor(@Body humor: HumorDTO): Response<Unit>
}

interface EvolucaoApi {
    @GET("evolution")
    suspend fun getHistoricos(): List<EvolucaoHistorico>
}

interface RecursosApi {
    @GET("recursos")
    suspend fun getRecursos(): List<Recurso>
}

interface TrilhasApi {
    @GET("trilhas")
    suspend fun getTrilhas(): List<Trilha>
}

interface VideosApi {
    @GET("videos")
    suspend fun getVideos(): List<Video>
}

interface SonsApi {
    @GET("sons")
    suspend fun getSons(): List<Som>
}

interface AfirmacoesApi {
    @GET("afirmacoes")
    suspend fun getAfirmacoesPorHumor(): List<AfirmacoesPorHumor>
}

interface AnaliseSintomasApi {
    @POST("sintomasEntrada")
    suspend fun postSintomas(@Body sintomas: SintomasEntry): List<Recomendacao>

    @GET("sintomas")
    suspend fun get(@Body sintomas: SintomasEntry): List<Recomendacao>
}

interface RecomendacoesApi {
    @GET("recomendacoes")
    suspend fun get(): List<Recomendacao>
}
