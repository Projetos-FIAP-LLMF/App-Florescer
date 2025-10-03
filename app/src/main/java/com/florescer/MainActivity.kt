package com.florescer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.florescer.data.HumorRepository
import com.florescer.data.repository.AuthRepository
import com.florescer.ui.navigation.NavGraph
import com.florescer.ui.theme.FlorescerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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