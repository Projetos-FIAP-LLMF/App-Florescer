package com.florescer.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.florescer.data.HumorRepository
import com.florescer.R
import com.florescer.ui.theme.*

@Composable
fun RecursosScreen(navController: NavHostController, humorRepository: HumorRepository) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val scope = rememberCoroutineScope()
    val syncMessage = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val humores = humorRepository.getHumoresLocais()
            humores.forEach { humor ->
                humorRepository.sendHumorToBackend(humor)
            }
        } catch (e: Exception) {
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.titulo3),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .height(100.dp)
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )

            if (syncMessage.value.isNotEmpty()) {
                Text(
                    syncMessage.value,
                    color = if (syncMessage.value.startsWith("✅")) Color.Green else Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("canalEscuta") },
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Canal de Escuta",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = RosaTexto
                    )
                    Text(
                        "Converse com alguém que pode te ouvir. Esse canal é anônimo e gratuito.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("orientacoes") },
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Orientações",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = RosaTexto
                    )
                    Text(
                        "Cuide da sua saúde mental: durma bem, alimente-se com equilíbrio e crie momentos de pausa.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0E0)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Alerta importante",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Text(
                        "Se estiver em emergência emocional, procure ajuda imediata.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("profissionais") },
                colors = CardDefaults.cardColors(containerColor = RosaBotao.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Orientação profissional",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Branco
                    )
                    Text(
                        "Consulte um profissional de saúde mental. Psicólogos e psiquiatras podem ajudar de forma segura e ética.",
                        fontSize = 14.sp,
                        color = Branco
                    )
                }
            }

            Button(
                onClick = { navController.navigate("evolucao") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Seus Dados", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
