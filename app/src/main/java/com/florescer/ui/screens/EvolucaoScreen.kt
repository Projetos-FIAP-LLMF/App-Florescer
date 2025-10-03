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
import com.florescer.data.repository.HumorRepository
import com.florescer.data.model.EvolucaoHistorico
import com.florescer.ui.theme.*
import kotlinx.coroutines.launch

/* ---------- Helpers ---------- */

private fun corDoHumorTexto(humor: String) = when (humor.uppercase()) {
    "AMOROSO" -> Color(0xFFFFC1E3)
    "FELIZ" -> Color(0xFFFFEB3B)
    "NEUTRO" -> Color(0xFF9E9E9E)
    "TRISTE" -> Color(0xFF2196F3)
    "BRAVO" -> Color(0xFFF44336)
    else -> Color.Gray
}

private fun scoreDoHumor(humor: String) = when (humor.uppercase()) {
    "AMOROSO" -> 5
    "FELIZ" -> 4
    "NEUTRO" -> 3
    "TRISTE" -> 2
    "BRAVO" -> 1
    else -> 0
}

private fun emojiDoHumor(score: Number): String {
    val num = score.toDouble()
    return when {
        num >= 4.5 -> "😄"
        num >= 3.5 -> "🙂"
        num >= 2.5 -> "😐"
        num >= 1.5 -> "😢"
        else -> "😭"
    }
}

/* ---------- Tela ---------- */

@Composable
fun EvolucaoScreen(
    navController: NavHostController,
    humorRepository: HumorRepository
) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var evolucao by remember { mutableStateOf<List<EvolucaoHistorico>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            evolucao = humorRepository.getHistoricoEvolucao()
        } catch (e: Exception) {
            error = e.message ?: "Falha ao carregar histórico."
        } finally {
            isLoading = false
        }
    }

    // KPIs
    val scores = evolucao.map { scoreDoHumor(it.humor) }.filter { it in 1..5 }
    val maiorHumor = scores.maxOrNull() ?: 0
    val menorHumor = scores.minOrNull() ?: 0
    val mediaHumor = scores.takeIf { it.isNotEmpty() }?.average() ?: 0.0
    val mediaEmoji = emojiDoHumor(mediaHumor)

    val fraseMotivacional = when {
        scores.isEmpty() -> "🌿 Comece registrando seu humor para ver sua evolução aqui."
        maiorHumor >= 4 -> "✨ Teve momentos positivos — celebre seus passos!"
        menorHumor <= 2 -> "🌧️ Dias desafiadores existem. Acolha-se com carinho."
        else -> "🌿 Siga cuidando de você, um dia de cada vez."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* HEADER — mesmo estilo das outras telas */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.titulo4),
                    contentDescription = "Logo do Florescer",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Sua evolução de humor 💫",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Um retrato gentil da sua semana. Acompanhe seu cuidado por aqui.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        when {
            isLoading -> {
                LoadingCard("Carregando histórico…")
            }

            error != null -> {
                ErrorCard(
                    message = error!!,
                    onRetry = {
                        isLoading = true
                        error = null
                        evolucao = emptyList()
                        scope.launch {
                            try {
                                evolucao = humorRepository.getHistoricoEvolucao()
                            } catch (e: Exception) {
                                error = e.message ?: "Falha ao carregar histórico."
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }

            evolucao.isEmpty() -> {
                EmptyStateCard(
                    title = "Sem registros ainda",
                    subtitle = "Faça seu primeiro check-in para acompanhar sua evolução 💖",
                    primaryAction = {
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Voltar", color = Branco) }
                    }
                )
            }

            else -> {
                /* SEÇÃO: Tendência da semana */
                SectionTitle("Humor da semana")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        evolucao.forEach { item ->
                            val score = scoreDoHumor(item.humor).coerceIn(0, 5)
                            val dia = item.data.takeLast(2).ifBlank { item.data }
                            val color = corDoHumorTexto(item.humor)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dia,
                                    modifier = Modifier.width(36.dp),
                                    color = Preto.copy(alpha = 0.7f),
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(14.dp)
                                        .background(Color(0x14000000), RoundedCornerShape(7.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth((score / 5f).coerceIn(0f, 1f))
                                            .height(14.dp)
                                            .background(color, RoundedCornerShape(7.dp))
                                    )
                                }

                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = emojiDoHumor(score),
                                    fontSize = 16.sp
                                )
                            }
                        }

                        // Legendinha simpática
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Quanto mais cheia a barra, melhor foi o humor do dia.",
                            color = Preto.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* SEÇÃO: Frase motivacional */
                Text(
                    text = fraseMotivacional,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RosaTexto,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                /* SEÇÃO: Resumo (Média / Mais alto / Mais baixo) */
                SectionTitle("Resumo")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryKpi(
                                label = "Média",
                                emoji = mediaEmoji,
                                detail = if (scores.isNotEmpty())
                                    String.format("%.1f/5", mediaHumor)
                                else "—"
                            )
                            SummaryKpi(
                                label = "Mais alto",
                                emoji = emojiDoHumor(maiorHumor),
                                detail = if (maiorHumor > 0) "$maiorHumor/5" else "—"
                            )
                            SummaryKpi(
                                label = "Mais baixo",
                                emoji = emojiDoHumor(menorHumor),
                                detail = if (menorHumor > 0) "$menorHumor/5" else "—"
                            )
                        }

                        // dica de leitura
                        Text(
                            text = "A média mostra seu clima geral; os picos e vales ajudam a notar padrões.",
                            color = Preto.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Voltar", color = Branco, fontSize = 18.sp) }
            }
        }
    }
}

/* ---------- Componentes auxiliares de UI ---------- */

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = RosaTexto,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
private fun LoadingCard(text: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = RosaTexto)
            Spacer(Modifier.height(8.dp))
            Text(text, color = RosaTexto, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ops! Algo deu errado", fontWeight = FontWeight.SemiBold, color = RosaTexto)
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                color = Preto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao)
            ) { Text("Tentar novamente", color = Branco) }
        }
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    subtitle: String,
    primaryAction: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.SemiBold, color = RosaTexto)
            Spacer(Modifier.height(6.dp))
            Text(
                subtitle,
                color = Preto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(12.dp))
            primaryAction()
        }
    }
}

@Composable
private fun SummaryKpi(
    label: String,
    emoji: String,
    detail: String
) {
    Surface(
        color = Color(0xFFFDF0F5),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .widthIn(min = 100.dp)
            .padding(end = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 20.sp)
            Spacer(Modifier.height(2.dp))
            Text(label, fontSize = 12.sp, color = Preto.copy(alpha = 0.7f))
            Spacer(Modifier.height(2.dp))
            Text(detail, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = RosaTexto)
        }
    }
}
