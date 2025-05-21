package com.florescer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.florescer.R


@Composable
fun EvolucaoScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    // Exemplo de dados fictícios (1 a 5 representa o "nível" de bem-estar)
    val dadosDeHumor = listOf(3, 2, 4, 5, 3, 4, 1)
    val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.titulo4),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .height(100.dp)
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Veja como seu humor tem variado ao longo da semana.",
                fontSize = 15.sp,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            // Gráfico simples (poderia ser substituído por uma lib externa futuramente)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    dadosDeHumor.forEachIndexed { index, valor ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(dias[index], fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = RosaTexto)
                            Text("Humor: $valor/5", fontSize = 14.sp)
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate("notificacoes") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver meus Lembretes", color = Branco, fontSize = 18.sp)
            }

            Button(
                onClick = { navController.navigate("recursos") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}

