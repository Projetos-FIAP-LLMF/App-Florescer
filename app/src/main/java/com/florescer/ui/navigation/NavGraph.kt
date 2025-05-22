package com.florescer.ui.navigation

import SonsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.florescer.ui.screens.*

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

        composable(
            route = "trilhas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "😐"
            TrilhasScreen(navController, mood = mood)
        }

        composable(
            route = "analiseSintomas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "😐"
            AnaliseSintomasScreen(navController, mood = mood)
        }

        composable("afirmacoes/{mood}") { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: ""
            AfirmacoesScreen(navController, mood)
        }


        composable("videos") {
            VideosScreen(navController)
        }

        composable("sons") {
            SonsScreen(navController)
        }
    }
}
