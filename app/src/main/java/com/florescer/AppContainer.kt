package com.florescer

import android.content.Context
import com.florescer.data.*
import com.florescer.data.network.*
import kotlinx.coroutines.runBlocking

class AppContainer(context: Context) {


    private val database: AppDatabase = AppDatabase.getInstance(context)


    private val humorDao = database.humorDao()
    private val tokenDao = database.tokenDao()


    private val humorApi = RetrofitInstance.api
    private val afirmacoesApi = RetrofitInstance.afirmacoesApi
    private val analiseSintomasApi = RetrofitInstance.analiseSintomasApi
    private val recomendacoesApi = RetrofitInstance.recomendacoesApi
    private val evolucaoApi = RetrofitInstance.evolucaoApi
    private val recursosApi = RetrofitInstance.recursosApi
    private val trilhasApi = RetrofitInstance.trilhasApi
    private val videosApi = RetrofitInstance.videosApi
    private val sonsApi = RetrofitInstance.sonsApi
    private val authApi = RetrofitInstance.authApi


    val authRepository = AuthRepository(
        api = authApi,
        tokenDao = tokenDao
    )

    init {

        RetrofitInstance.setTokenProvider {

            runBlocking {
                authRepository.getAuthToken()
            }
        }
    }
    val humorRepository = HumorRepository(
        humorApi = humorApi,
        afirmacoesApi = afirmacoesApi,
        analiseSintomasApi = analiseSintomasApi,
        recomendacoesApi = recomendacoesApi,
        evolucaoApi = evolucaoApi,
        recursosApi = recursosApi,
        trilhasApi = trilhasApi,
        videosApi = videosApi,
        sonsApi = sonsApi,
        dao = humorDao,
        authRepository = authRepository
    )
}