package com.florescer.ui.navigation

import SonsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.florescer.data.HumorRepository
import com.florescer.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController, humorRepository: HumorRepository) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("mood") {
            MoodScreen(navController, humorRepository)
        }
        composable("recursos") {
            RecursosScreen(navController, humorRepository)
        }

        composable("evolucao") {
            EvolucaoScreen(navController, humorRepository)
        }

        composable("notificacoes") {
            NotificacoesScreen(navController)
        }

        composable(
            route = "trilhas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "feliz"
            TrilhasScreen(navController, mood = mood)
        }

        composable(
            route = "analiseSintomas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            AnaliseSintomasScreen(navController, mood, humorRepository)
        }

        composable("afirmacoes/{mood}") { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            AfirmacoesScreen(navController, mood, humorRepository)
        }


        composable("videos") {
            VideosScreen(navController)
        }

        composable("sons") {
            SonsScreen(navController)
        }
    }
}
