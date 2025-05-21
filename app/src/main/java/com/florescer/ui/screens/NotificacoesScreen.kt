package com.florescer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.ui.theme.*

@Composable
fun NotificacoesScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val dicasDoDia = listOf(
        "🧘 Respire fundo. Tente uma meditação guiada de 5 minutos.",
        "💧 Beba água e alongue-se um pouco.",
        "📖 Leia algo que te inspire por 10 minutos.",
        "🌳 Se puder, vá até uma janela ou dê uma volta ao ar livre.",
        "🎵 Escute uma música que te acalme ou te anime.",
        "🤗 Lembre-se: pedir ajuda é um ato de coragem."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.titulo5),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .height(100.dp)
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Com base no seu humor, aqui vão sugestões para cuidar de você hoje:",
                fontSize = 16.sp,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            dicasDoDia.forEach { dica ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dica,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Preto
                    )
                }
            }
            Button(
                onClick = { navController.navigate("evolucao") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}

