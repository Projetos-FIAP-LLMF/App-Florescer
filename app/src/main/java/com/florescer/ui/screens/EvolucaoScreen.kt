package com.florescer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.data.HumorRepository
import com.florescer.data.model.EvolucaoHistorico
import com.florescer.ui.theme.*
import kotlinx.coroutines.launch

fun corDoHumorTexto(humor: String) = when (humor.lowercase()) {
    "amoroso" -> Color(0xFFFFC1E3)
    "feliz" -> Color(0xFFFFEB3B)
    "neutro" -> Color(0xFF9E9E9E)
    "triste" -> Color(0xFF2196F3)
    "bravo" -> Color(0xFFF44336)
    else -> Color.Gray
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

@Composable
fun EvolucaoScreen(
    navController: NavHostController, humorRepository: HumorRepository
) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var evolucao by remember { mutableStateOf<List<EvolucaoHistorico>>(emptyList()) }

    val scope = rememberCoroutineScope()

    suspend fun loadData() {
        isLoading = true
        error = null
        try {
            evolucao = humorRepository.getHistoricoEvolucao()
        } catch (e: Exception) {
            error = e.message ?: "Falha ao carregar histórico."
            evolucao = emptyList()
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { loadData() }

    val dias = evolucao.map { it.data.takeLast(2).ifBlank { it.data } }
    val humores = evolucao.map { it.humor }

    val humorNumerico = humores.map {
        when (it.lowercase()) {
            "amoroso" -> 5
            "feliz" -> 4
            "neutro" -> 3
            "triste" -> 2
            "bravo" -> 1
            else -> 0
        }
    }.filter { it > 0 }

    val maiorHumor = humorNumerico.maxOrNull() ?: 0
    val menorHumor = humorNumerico.minOrNull() ?: 0
    val mediaHumor = if (humorNumerico.isNotEmpty()) humorNumerico.average() else 0.0
    val mediaEmoji = emojiDoHumor(mediaHumor)

    val fraseMotivacional = when {
        humorNumerico.isEmpty() -> "🌿 Comece registrando seu humor para ver sua evolução aqui."
        maiorHumor >= 4 -> "✨ Sua semana teve momentos positivos!"
        menorHumor <= 2 -> "🌧️ Perceba os dias mais difíceis. Se acolha!"
        else -> "🌿 Você está se cuidando bem! Continue!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        when {
            isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = RosaTexto)
                    Text(
                        text = "Carregando histórico...",
                        color = RosaTexto,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error!!, color = RosaTexto, textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { scope.launch { loadData() } },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                        shape = RoundedCornerShape(30)
                    ) {
                        Text("Tentar novamente", color = Branco)
                    }
                }
            }

            evolucao.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sem registros ainda",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RosaTexto
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Faça seu primeiro check-in para acompanhar sua evolução 💖",
                        color = RosaTexto,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                        shape = RoundedCornerShape(30)
                    ) {
                        Text("Voltar", color = Branco)
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            evolucao.forEachIndexed { index, item ->
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = dias.getOrNull(index) ?: "Dia",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = RosaTexto
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(14.dp)
                                            .background(
                                                Color.LightGray.copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(7.dp)
                                            )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(
                                                    fraction = when (item.humor.lowercase()) {
                                                        "amoroso" -> 1f
                                                        "feliz" -> 0.8f
                                                        "neutro" -> 0.6f
                                                        "triste" -> 0.4f
                                                        "bravo" -> 0.2f
                                                        else -> 0.1f
                                                    }
                                                )
                                                .height(14.dp)
                                                .background(
                                                    corDoHumorTexto(item.humor),
                                                    shape = RoundedCornerShape(7.dp)
                                                )
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "📊 Resumo:",
                                fontWeight = FontWeight.Bold,
                                color = RosaTexto,
                                fontSize = 16.sp
                            )
                            Text("🔝 Maior humor: ${emojiDoHumor(maiorHumor)}")
                            Text("🔻 Menor humor: ${emojiDoHumor(menorHumor)}")
                            Text("📈 Média: $mediaEmoji")
                        }
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                        shape = RoundedCornerShape(30),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Voltar", color = Branco, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
