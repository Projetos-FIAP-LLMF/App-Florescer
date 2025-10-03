package com.florescer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.florescer.data.repository.AuthRepository
import com.florescer.data.repository.HumorRepository
import com.florescer.ui.navigation.NavGraph
import com.florescer.ui.theme.FlorescerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        // Obtém o container da Application
        val appContainer = (application as MyApplication).container

        setContent {
            FlorescerApp(
                humorRepository = appContainer.humorRepository,
                authRepository = appContainer.authRepository
            )
        }
    }
}

@Composable
fun FlorescerApp(
    humorRepository: HumorRepository,
    authRepository: AuthRepository
) {
    FlorescerTheme {
        val navController = rememberNavController()
        NavGraph(
            navController = navController,
            humorRepository = humorRepository,
            authRepository = authRepository
        )
    }
}