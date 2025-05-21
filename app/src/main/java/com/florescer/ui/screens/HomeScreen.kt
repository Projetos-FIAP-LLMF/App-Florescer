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
                text = "Nossa geração está exausta, ansiosa e sozinha. A Florescer é a ponte entre a dor silenciosa e o cuidado que transforma. Você conhece alguém que nunca precisou de acolhimento emocional?\n" +
                        "\n" +
                        "A Florescer nasce como ponte. Entre o colapso e o recomeço. Entre a dor invisível e o cuidado que transforma. Porque no fundo, a gente só quer respirar com leveza.",
                fontSize = 15.sp,
                color = Preto,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Não é apenas um app de bem-estar.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "É um espaço de pausa no caos. Um lembrete de que seu corpo é corpo, não máquina. Um convite pra desacelerar, se ouvir, se acolher. Aqui, cada pessoa é respeitada no seu tempo — e cuidada com afeto. O que você já fez por você hoje?",
                fontSize = 15.sp,
                color = Preto,
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
                text = "Ao clicar no botão você aceita a Política de Dados",
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = Preto
            )
        }
    }
}
