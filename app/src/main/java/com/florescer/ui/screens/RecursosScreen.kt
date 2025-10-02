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
import kotlinx.coroutines.launch

@Composable
fun RecursosScreen(
    navController: NavHostController,
    humorRepository: HumorRepository
) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))
    val context = LocalContext.current

    fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val scope = rememberCoroutineScope()
    var syncMessage by remember { mutableStateOf("") }
    var isSyncing by remember { mutableStateOf(false) }

    // BOTÃO MANUAL DE SINCRONIZAÇÃO
    fun sincronizarDados() {
        isSyncing = true
        syncMessage = "⏳ Sincronizando..."

        scope.launch {
            try {
                val humores = humorRepository.getHumoresLocais()
                var sucessos = 0
                var falhas = 0

                humores.forEach { humor ->
                    try {
                        humorRepository.sendHumorToBackend(humor)
                        sucessos++
                    } catch (e: Exception) {
                        falhas++
                        e.printStackTrace()
                    }
                }

                syncMessage = if (falhas == 0) {
                    "✅ $sucessos registros sincronizados!"
                } else {
                    "⚠️ $sucessos enviados, $falhas falharam"
                }
            } catch (e: Exception) {
                syncMessage = "❌ Erro ao sincronizar dados"
                e.printStackTrace()
            } finally {
                isSyncing = false
            }
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

            if (syncMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            syncMessage.startsWith("✅") -> Color.Green.copy(alpha = 0.2f)
                            syncMessage.startsWith("❌") -> Color.Red.copy(alpha = 0.2f)
                            else -> Color.Gray.copy(alpha = 0.2f)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        syncMessage,
                        modifier = Modifier.padding(12.dp),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                onClick = { sincronizarDados() },
                enabled = !isSyncing,
                colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Branco,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Sincronizar Dados", color = Branco, fontSize = 16.sp)
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
                modifier = Modifier.fillMaxWidth(),
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
                onClick = { navController.navigate("trilhas/neutro") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
