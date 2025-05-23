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
import com.florescer.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.florescer.R
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(navController: NavHostController) {
    // Gradiente de fundo vertical
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.titulo),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .height(100.dp)
                    .size(400.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "A Florescer é uma pausa no caos.\n" +
                        "Um espaço de acolhimento emocional, onde você desacelera, se ouve e se cuida.\n" +
                        "Aqui, cada pessoa é respeitada no seu tempo — e cuidada com afeto.\n",
                fontSize = 15.sp,
                color = Preto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "O que você já fez por você hoje?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { navController.navigate("mood") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Começar", color = Branco, fontSize = 20.sp)
            }

            Text(
                text = "Ao clicar no botão você não é identificado no processo de utilização do aplicativo.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Preto
            )
        }
    }
}
