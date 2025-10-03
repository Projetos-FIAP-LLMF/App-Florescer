package com.florescer.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.florescer.R
import com.florescer.data.HumorRepository
import com.florescer.data.model.Recomendacao
import com.florescer.ui.theme.*

@Composable
fun AnaliseSintomasScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository
) {
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))

    val moodGif = when (mood.lowercase()) {
        "feliz" -> R.drawable.happycat
        "triste" -> R.drawable.sad
        "bravo" -> R.drawable.angry
        "ansioso", "ansiedade", "enjoado" -> R.drawable.anxious
        "neutro" -> R.drawable.neutral
        "amoroso" -> R.drawable.love
        else -> R.drawable.neutral
    }

    var recomendacoes by remember { mutableStateOf<List<Recomendacao>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(mood) {
        isLoading = true
        error = null
        try {
            recomendacoes = repository.getRecomendacoes(limit = 5)
        } catch (e: Exception) {
            error = e.message ?: "Erro desconhecido"
        } finally {
            isLoading = false
        }
    }

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(moodGif)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Analisamos como você está hoje",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Aqui vão sugestões simples e carinhosas para o seu momento.",
                fontSize = 14.sp,
                color = Preto.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(18.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(color = RosaTexto)
                }

                error != null -> {
                    ErrorCard(message = error!!)
                }

                recomendacoes.isEmpty() -> {
                    EmptyStateCard()
                }

                else -> {
                    SectionHeader(title = "Recomendações")
                    Spacer(Modifier.height(8.dp))
                    recomendacoes.forEachIndexed { idx, rec ->
                        val txt = rec.descricao ?: return@forEachIndexed
                        RecommendationCard(
                            index = idx + 1,
                            text = txt,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("trilhas/$mood") },
            colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
            shape = RoundedCornerShape(28),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Ver Trilha de Bem-Estar",
                color = Branco,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = { navController.navigate("avaliacao") },
            shape = RoundedCornerShape(28),
            modifier = Modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
        ) {
            Text("Fazer Minha Autoavaliação", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Preto
    )
}

@Composable
private fun RecommendationCard(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Branco.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberBadge(number = index)
            Spacer(Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Preto,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun NumberBadge(number: Int) {
    Surface(
        color = RosaBotao,
        shape = RoundedCornerShape(50),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.size(width = 28.dp, height = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Branco
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ops! Algo deu errado",
                fontWeight = FontWeight.SemiBold,
                color = Preto
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = message,
                color = Preto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sem recomendações por agora",
                fontWeight = FontWeight.SemiBold,
                color = Preto
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Respire fundo, beba água e cuide de você 💖",
                color = Preto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
    }
}
