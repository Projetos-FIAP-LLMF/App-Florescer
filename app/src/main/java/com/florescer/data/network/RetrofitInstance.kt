package com.florescer.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://florescer-backend-5.onrender.com/"

    // Token provider - será injetado pelo AppContainer
    private var tokenProvider: (() -> String?)? = null // ⬅️ CORREÇÃO: Adicione "?"

    fun setTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor {
                tokenProvider?.invoke() // ⬅️ Isso já retorna String?
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("User-Agent", "Florescer-Android-App/1.0")
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Suas APIs (mantenha igual)
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