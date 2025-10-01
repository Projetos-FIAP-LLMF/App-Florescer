package com.florescer.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    // Use a mesma URL em todo o app
    private const val BASE_URL = "https://florescer-backend-5.onrender.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
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

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
}