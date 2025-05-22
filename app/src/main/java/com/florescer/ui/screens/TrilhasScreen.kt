package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.Branco
import com.florescer.ui.theme.GradienteBottom
import com.florescer.ui.theme.GradienteTop
import com.florescer.ui.theme.RosaBotao
import com.florescer.ui.theme.RosaTexto

@Composable
fun TrilhasScreen(navController: NavHostController, mood: String) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    val trilhas = listOf(
        "✨ Afirmações" to "afirmacoes/$mood",
        "🎥 Vídeos Relaxantes" to "videos",
        "🎶 Sons de Meditação" to "sons"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Escolha sua trilha de bem-estar 🌸",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Cada trilha é um convite gentil ao seu autocuidado.",
                fontSize = 14.sp,
                color = RosaTexto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            trilhas.forEach { (label, route) ->
                Button(
                    onClick = { navController.navigate(route) },
                    colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(label, fontSize = 16.sp, color = Branco, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
