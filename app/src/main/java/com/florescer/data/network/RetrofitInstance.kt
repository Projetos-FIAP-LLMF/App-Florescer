package com.florescer.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://florescer-mock-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: HumorApi by lazy {
        retrofit.create(HumorApi::class.java)
    }

    val afirmacoesApi: AfirmacoesApi by lazy {
        retrofit.create(AfirmacoesApi::class.java)
    }

    val analiseSintomasApi: AnaliseSintomasApi by lazy {
        retrofit.create(AnaliseSintomasApi::class.java)
    }

    val recomendacoesApi: RecomendacoesApi by lazy {
        retrofit.create(RecomendacoesApi::class.java)
    }

    val evolucaoApi: EvolucaoApi by lazy {
        retrofit.create(EvolucaoApi::class.java)
    }
}
