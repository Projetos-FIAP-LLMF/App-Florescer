package com.florescer.data.network

import com.florescer.data.model.HumorDTO
import com.florescer.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface HumorApi {
    @GET("humor")
    suspend fun getHumores(): List<Humor>

    @POST("checkin")
    suspend fun postHumor(
        @Header("X-User-Id") userId: String,
        @Body humor: HumorDTO
    ): HumorDTO
}

interface EvolucaoApi {
    @GET("evolution")
    suspend fun getHistoricos(
        @Header("X-User-Id") userId: String,
        @Query("dateFrom") dateFrom: String? = null,
        @Query("dateTo") dateTo: String? = null,
        @Query("limit") limit: Int? = null
    ): List<EvolucaoHistorico>
}


interface RecursosApi {
    @GET("resources")
    suspend fun getRecursos(@Header("X-User-Id") userId: String, @Query("limit") limit: Int? = 10): List<Recurso>
}

interface TrilhasApi {
    @GET("resources") // se for "trilhas" mesmo, ajustar endpoint
    suspend fun getTrilhas(@Header("X-User-Id") userId: String, @Query("limit") limit: Int? = 10): List<Trilha>
}

interface VideosApi {
    @GET("videos")
    suspend fun getVideos(@Header("X-User-Id") userId: String, @Query("limit") limit: Int? = 10): List<Video>
}

interface SonsApi {
    @GET("sounds")
    suspend fun getSons(@Header("X-User-Id") userId: String, @Query("limit") limit: Int? = 10): List<Som>
}

interface AfirmacoesApi {
    @GET("affirmations")
    suspend fun getAfirmacoesPorHumor(
        @Header("X-User-Id") userId: String,
        @Query("limit") limit: Int? = 3,
        @Query("mood") mood: String? = null
    ): List<AfirmacoesPorHumor>
}

interface AnaliseSintomasApi {
    @POST("assessments/analyze")
    suspend fun postSintomas(
        @Header("X-User-Id") userId: String,
        @Body request: SintomasEntry
    ): List<Recomendacao>
}


interface RecomendacoesApi {
    @GET("recommendation")
    suspend fun get(@Header("X-User-Id") userId: String, @Query("limit") limit: Int? = 5): List<Recomendacao>
}

