package com.florescer.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.ui.theme.*

@Composable
fun EvolucaoScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val dadosDeHumor = listOf(3, 2, 4, 5, 3, 4, 1)
    val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

    val maiorHumor = dadosDeHumor.maxOrNull() ?: 0
    val menorHumor = dadosDeHumor.minOrNull() ?: 0
    val mediaHumor = dadosDeHumor.average()

    val fraseMotivacional = when {
        maiorHumor >= 4 -> "✨ Sua semana teve momentos positivos!"
        menorHumor <= 2 -> "🌧️ Perceba os dias mais difíceis. Se acolha!"
        else -> "🌿 Você está se cuidando bem! Continue!"
    }

    val humoresDoMes = List(30) { (1..5).random() }  // Dados fictícios para o mês

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.titulo4),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .height(80.dp)
                    .size(280.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Humor da Semana",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            // Gráfico de humor semanal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    dadosDeHumor.forEachIndexed { index, valor ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = dias[index],
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = RosaTexto
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(7.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = (valor.coerceAtLeast(1)) / 5f)
                                        .height(14.dp)
                                        .background(corDoHumor(valor), shape = RoundedCornerShape(7.dp))
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = fraseMotivacional,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            // Resumo com emojis
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("📊 Resumo:", fontWeight = FontWeight.Bold, color = RosaTexto, fontSize = 16.sp)
                    Text("🔝 Maior humor: ${emojiDoHumor(maiorHumor)}")
                    Text("🔻 Menor humor: ${emojiDoHumor(menorHumor)}")
                    Text("📈 Média: ${emojiDoHumor(mediaHumor)}")
                }
            }

            Button(
                onClick = { navController.navigate("trilhas/{mood}") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun corDoHumor(valor: Int): Color {
    return when (valor) {
        1 -> Color(0xFFFF8B8B)
        2 -> Color(0xFFFAB68D)
        3 -> Color(0xFFFFE895)
        4 -> Color(0xFF9DF1A9)
        5 -> Color(0xFFA8C1E8)
        else -> Color.Gray
    }
}

fun emojiDoHumor(valor: Number): String {
    val num = valor.toDouble()
    return when {
        num >= 4.5 -> "😄"
        num >= 3.5 -> "🙂"
        num >= 2.5 -> "😐"
        num >= 1.5 -> "😢"
        else -> "😭"
    }
}
