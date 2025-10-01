package com.florescer.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://florescer-backend-5.onrender.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: HumorApi by lazy { retrofit.create(HumorApi::class.java) }
    val afirmacoesApi: AfirmacoesApi by lazy { retrofit.create(AfirmacoesApi::class.java) }
    val analiseSintomasApi: AnaliseSintomasApi by lazy { retrofit.create(AnaliseSintomasApi::class.java) }
    val recomendacoesApi: RecomendacoesApi by lazy { retrofit.create(RecomendacoesApi::class.java) }
    val evolucaoApi: EvolucaoApi by lazy { retrofit.create(EvolucaoApi::class.java) }

    val recursosApi: RecursosApi by lazy { retrofit.create(RecursosApi::class.java) }
    val trilhasApi: TrilhasApi by lazy { retrofit.create(TrilhasApi::class.java) }
    val videosApi: VideosApi by lazy { retrofit.create(VideosApi::class.java) }
    val sonsApi: SonsApi by lazy { retrofit.create(SonsApi::class.java) }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
}
