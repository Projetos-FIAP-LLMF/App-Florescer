package com.florescer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.florescer.ui.navigation.NavGraph
import com.florescer.ui.theme.FlorescerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlorescerApp()
        }
    }
}

@Composable
fun FlorescerApp() {
    FlorescerTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController)
    }
}
