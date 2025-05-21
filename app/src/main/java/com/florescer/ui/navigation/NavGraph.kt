package com.florescer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.florescer.ui.screens.HomeScreen
import com.florescer.ui.screens.MoodScreen
import com.florescer.ui.screens.RecursosScreen
import com.florescer.ui.screens.EvolucaoScreen
import com.florescer.ui.screens.NotificacoesScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("mood") {
            MoodScreen(navController)
        }
        composable("recursos") {
            RecursosScreen(navController)
        }
        composable("evolucao") {
            EvolucaoScreen(navController)
        }
        composable("notificacoes") {
            NotificacoesScreen(navController)
        }

    }
}
