package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.florescer.ui.theme.*
import com.florescer.R

@Composable
fun AnaliseSintomasScreen(navController: NavHostController, mood: String) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val moodGif = when (mood) {
        "😄" -> R.drawable.happycat
        "😢" -> R.drawable.sad
        "😡" -> R.drawable.angry
        "😰" -> R.drawable.anxious
        "😐" -> R.drawable.neutral
        "🥰" -> R.drawable.love
        else -> R.drawable.neutral
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(moodGif)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = "Seu humor do dia em gif de gatinho",
                modifier = Modifier.size(200.dp)
            )

            Text(
                "Analisamos como você está hoje…",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                "Sugerimos que explore uma trilha de bem-estar para acolher suas emoções. 🌻",
                fontSize = 16.sp,
                color = Preto,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { navController.navigate("trilhas/$mood") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Trilha de Bem-Estar", color = Branco, fontSize = 18.sp)
            }

            Button(
                onClick = { navController.navigate("avaliacao") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fazer Minha Autoavaliação", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
