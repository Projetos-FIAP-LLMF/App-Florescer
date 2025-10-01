package com.florescer

import android.content.Context
import com.florescer.data.AppDatabase
import com.florescer.data.AuthRepository
import com.florescer.data.HumorRepository
import com.florescer.data.network.RetrofitInstance
import com.florescer.data.network.AuthApi



class AppContainer(context: Context) {

    // Database (Room)
    private val database: AppDatabase = AppDatabase.getInstance(context)

    // DAOs
    private val humorDao = database.humorDao()
    private val tokenDao = database.tokenDao()

    // APIs (via RetrofitInstance)
    private val humorApi = RetrofitInstance.api
    private val afirmacoesApi = RetrofitInstance.afirmacoesApi
    private val analiseSintomasApi = RetrofitInstance.analiseSintomasApi
    private val recomendacoesApi = RetrofitInstance.recomendacoesApi
    private val evolucaoApi = RetrofitInstance.evolucaoApi
    private val authApi = RetrofitInstance.authApi

    // Repositories
    val humorRepository = HumorRepository(
        humorApi = humorApi,
        afirmacoesApi = afirmacoesApi,
        analiseSintomasApi = analiseSintomasApi,
        recomendacoesApi = recomendacoesApi,
        evolucaoApi = evolucaoApi,
        dao = humorDao
    )

    val authRepository = AuthRepository(
        authApi = authApi,
        tokenDao = tokenDao,
        api = authApi
    )
}