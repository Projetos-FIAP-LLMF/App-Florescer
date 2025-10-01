package com.florescer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.florescer.ui.screens.*
import com.florescer.ui.auth.AuthScreen
import com.florescer.data.AuthRepository
import com.florescer.data.HumorRepository
import com.florescer.ui.auth.AuthViewModel
import com.florescer.ui.auth.AuthViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    humorRepository: HumorRepository,
    authRepository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        // Tela de autenticação
        composable("auth") {
            AuthScreen(
                authRepository = authRepository,
                onTokenObtido = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

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

        // Removi a rota "sons" pois SonsScreen não foi fornecida
        // Se você tiver essa tela, adicione aqui:
        // composable("sons") {
        //     SonsScreen(navController)
        // }
    }
}