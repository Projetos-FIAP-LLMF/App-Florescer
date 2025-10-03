package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.florescer.data.repository.HumorRepository
import com.florescer.data.model.Recomendacao
import com.florescer.ui.theme.*

@Composable
fun AnaliseSintomasScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository
) {
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))

    val (moodEmoji, moodColor, moodNice) = moodUi(mood)

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

        // HEADER
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Mood pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = moodColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(100)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = moodEmoji,
                        fontSize = 18.sp,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = moodNice,
                        color = moodColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Gif em avatar-card
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            color = moodColor.copy(alpha = 0.10f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        tonalElevation = 0.dp,
                        shadowElevation = 2.dp
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(moodGif)
                                .decoderFactory(GifDecoder.Factory())
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Analisamos como você está hoje",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Aqui vão sugestões simples e carinhosas para o seu momento.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // CONTEÚDO SCROLL
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SectionHeader(title = "Recomendações")

            Spacer(Modifier.height(8.dp))

            when {
                isLoading -> {
                    // Loading em card fofinho
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = RosaTexto, strokeWidth = 3.dp)
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "Gerando sugestões…",
                                color = RosaTexto,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                error != null -> {
                    ErrorCard(message = error!!)
                }

                recomendacoes.isEmpty() -> {
                    EmptyStateCard()
                }

                else -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recomendacoes.forEachIndexed { idx, rec ->
                            val txt = rec.descricao ?: return@forEachIndexed
                            RecommendationCard(
                                index = idx + 1,
                                text = txt,
                                accent = moodColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // CTAs
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
            Text(
                "Fazer Minha Autoavaliação",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/* ==== UI HELPERS ==== */

private fun moodUi(mood: String): Triple<String, Color, String> {
    return when (mood.lowercase()) {
        "feliz"    -> Triple("😊", Color(0xFFFFC107), "Feliz")
        "triste"   -> Triple("😢", Color(0xFF2196F3), "Triste")
        "bravo"    -> Triple("😠", Color(0xFFF44336), "Bravo")
        "ansioso", "ansiedade" -> Triple("😰", Color(0xFF7E57C2), "Ansioso(a)")
        "enjoado"  -> Triple("🤢", Color(0xFF66BB6A), "Indisposto(a)")
        "amoroso"  -> Triple("💖", Color(0xFFFF80AB), "Amoroso(a)")
        "neutro"   -> Triple("😐", Color(0xFF9E9E9E), "Neutro")
        else       -> Triple("🌿", Color(0xFF9E9E9E), mood.replaceFirstChar { it.uppercase() })
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Preto,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun RecommendationCard(
    index: Int,
    text: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
        color = RosaBotao, // fixo rosa 💗
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
