package com.florescer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.florescer.data.AppDatabase
import com.florescer.data.HumorRepository
import com.florescer.data.network.HumorApi
import com.florescer.data.network.RetrofitInstance
import com.florescer.ui.navigation.NavGraph
import com.florescer.ui.theme.FlorescerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        val database = AppDatabase.getInstance(this)
        val humorDao = database.humorDao()

        val humorApi = RetrofitInstance.api
        val afirmacoesApi = RetrofitInstance.afirmacoesApi
        val analiseSintomasApi = RetrofitInstance.analiseSintomasApi
        val recomendacoesApi = RetrofitInstance.recomendacoesApi
        val evolucaoApi = RetrofitInstance.evolucaoApi


        val humorRepository = HumorRepository(
            humorApi,
            afirmacoesApi,
            analiseSintomasApi,
            recomendacoesApi,
            evolucaoApi,
            humorDao
        )
        setContent {
            FlorescerApp(humorRepository)
        }
    }
}

@Composable
fun FlorescerApp(humorRepository: HumorRepository) {
    FlorescerTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController, humorRepository = humorRepository)
    }
}
