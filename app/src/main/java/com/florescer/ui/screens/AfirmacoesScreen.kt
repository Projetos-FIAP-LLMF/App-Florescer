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
import com.florescer.ui.theme.*

@Composable
fun AfirmacoesScreen(navController: NavHostController, mood: String) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val afirmacoes = when (mood) {
        "😄" -> listOf(
            "💬 Continue espalhando sua alegria! 😊",
            "💬 Sua felicidade inspira quem está ao seu redor!"
        )
        "😢" -> listOf(
            "💬 Tudo bem não estar bem o tempo todo. Você é forte! 💛",
            "💬 Sua tristeza merece acolhimento e carinho. Respire e se abrace. 🤍"
        )
        "😡" -> listOf(
            "💬 Sua raiva é válida, mas não define quem você é. 🌿",
            "💬 Respire fundo. Você está no controle das suas emoções. 🧘‍♀️"
        )
        "😰" -> listOf(
            "💬 A ansiedade não é mais forte que você. Um passo de cada vez. 🌻",
            "💬 Você merece paz. Respire e acolha sua jornada. 💫"
        )
        "😐" -> listOf(
            "💬 Mesmo nos dias neutros, você é importante. 🌼",
            "💬 A sua presença no mundo faz diferença. 💚"
        )
        "🥰" -> listOf(
            "💬 Que bom sentir amor! Compartilhe e receba afeto. ❤️",
            "💬 O amor que você sente torna o mundo mais leve. 🌸"
        )
        else -> listOf(
            "💬 Você é suficiente, exatamente como é. 🌿"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Afirmações para você 🌿",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Escolha a afirmação que mais acolhe seu momento.",
                fontSize = 14.sp,
                color = RosaTexto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            afirmacoes.forEach { afirmacao ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RosaBotao),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = afirmacao,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Branco,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
