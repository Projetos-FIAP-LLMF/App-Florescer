package com.florescer.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.florescer.ui.screens.*
import com.florescer.data.AuthRepository
import com.florescer.data.HumorRepository
import com.florescer.ui.auth.AuthViewModel
import com.florescer.ui.auth.AuthViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    humorRepository: HumorRepository,
    authRepository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )
            HomeScreen(navController, authViewModel)
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

        composable("avaliacao") {
            AvaliacaoRiscosScreen(navController)
        }

        composable(
            route = "trilhas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            TrilhasScreen(navController, mood = mood)
        }

        composable(
            route = "analiseSintomas/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            AnaliseSintomasScreen(navController, mood, humorRepository)
        }

        composable(
            route = "afirmacoes/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            AfirmacoesScreen(navController, mood, humorRepository)
        }

        composable(
            route = "videos/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            VideosScreen(navController, mood, humorRepository)
        }


        composable(
            route = "sons/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "neutro"
            SonsScreen(navController, humorRepository, mood)
        }
    }
}