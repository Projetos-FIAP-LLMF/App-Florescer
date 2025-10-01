package com.florescer.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.data.HumorRepository
import com.florescer.ui.theme.*

@Composable
fun RecursosScreen(navController: NavHostController, humorRepository: HumorRepository) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))
    val context = LocalContext.current

    fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val scope = rememberCoroutineScope()
    val syncMessage = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val humores = humorRepository.getHumoresLocais()
            humores.forEach { humor ->
                humorRepository.sendHumorToBackend(humor, "null")
            }
            syncMessage.value = "✅ Dados sincronizados com sucesso!"
        } catch (e: Exception) {
            syncMessage.value = "❌ Erro ao sincronizar dados"
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
                    .clickable { abrirLink("https://www.cvv.org.br/") },
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "CVV - Centro de Valorização da Vida",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = RosaTexto
                    )
                    Text(
                        "Fale com alguém que pode te ouvir. Canal anônimo e gratuito.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { abrirLink("https://www.gov.br/saude/pt-br/composicao/saps/saude-mental") },
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Serviços públicos de saúde mental",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = RosaTexto
                    )
                    Text(
                        "Encontre apoio profissional próximo de você.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { abrirLink("https://www.mapasaudemental.com.br/") },
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Mapa da Saúde Mental",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = RosaTexto
                    )
                    Text(
                        "Encontre psicólogos e serviços de saúde mental em sua região.",
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("orientacoes") },
                colors = CardDefaults.cardColors(containerColor = RosaBotao.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Orientações",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Branco
                    )
                    Text(
                        "Dicas de cuidado: durma bem, alimente-se com equilíbrio e crie momentos de pausa.",
                        fontSize = 14.sp,
                        color = Branco
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
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

            Button(
                onClick = { navController.navigate("evolucao") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Seus Dados", color = Branco, fontSize = 18.sp)
            }

            Button(
                onClick = { navController.navigate("trilhas/feliz") }, // ou outro `mood` default
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
