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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.data.repository.HumorRepository
import com.florescer.data.model.Afirmacao
import com.florescer.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AfirmacoesScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository
) {
    var afirmacoes by remember { mutableStateOf<List<Afirmacao>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Carga inicial
    LaunchedEffect(mood) {
        isLoading = true
        error = null
        try {
            afirmacoes = repository.getAfirmacoesPorHumor(mood = mood, limit = 3) // 👈 usa limit=3
        } catch (e: Exception) {
            error = e.message ?: "Erro ao carregar afirmações"
        } finally {
            isLoading = false
        }
    }

    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))
    val (emoji, accent, moodNice) = moodUi(mood)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER — mood pill + títulos
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(accent.copy(alpha = 0.12f), RoundedCornerShape(100))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = emoji, fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = moodNice,
                        color = accent,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Afirmações para você 🌿",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Leituras curtinhas e carinhosas para acolher seu momento.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        when {
            isLoading -> {
                LoadingCard()
            }

            error != null -> {
                ErrorCard(
                    message = error!!,
                    onRetry = {
                        isLoading = true
                        error = null
                        afirmacoes = emptyList()
                        scope.launch {
                            try {
                                afirmacoes = repository.getAfirmacoesPorHumor(mood = mood, limit = 3)
                            } catch (e: Exception) {
                                error = e.message ?: "Erro ao carregar afirmações"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }

            afirmacoes.isEmpty() -> {
                EmptyStateCard()
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    afirmacoes.forEach { afirmacao ->
                        AffirmationCard(text = afirmacao.text)
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Ações
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
            ) {
                Text("Voltar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { navController.navigate("trilhas/$mood") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Outras trilhas",
                    color = Branco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/* ---------- COMPONENTES ---------- */

@Composable
private fun AffirmationCard(text: String) {
    val clipboard = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(color = RosaBotao, shape = CircleShape) {
                Box(modifier = Modifier.size(8.dp))
            }
            Spacer(Modifier.height(10.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RosaTexto,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(12.dp))

            FilledTonalButton(
                onClick = { clipboard.setText(AnnotatedString(text)) },
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Copiar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = RosaTexto)
            Spacer(Modifier.height(8.dp))
            Text("Carregando afirmações…", color = RosaTexto, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
            ) {
                Text("Tentar novamente", color = Branco)
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sem afirmações por agora", fontWeight = FontWeight.SemiBold, color = RosaTexto)
            Spacer(Modifier.height(6.dp))
            Text(
                "Respire fundo, beba água e cuide de você 💖",
                color = Preto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
    }
}

/* ---------- HELPERS ---------- */

private fun moodUi(mood: String): Triple<String, Color, String> {
    return when (mood.lowercase()) {
        "feliz" -> Triple("😊", Color(0xFFFFC107), "Feliz")
        "triste" -> Triple("😢", Color(0xFF2196F3), "Triste")
        "bravo" -> Triple("😠", Color(0xFFF44336), "Bravo")
        "ansioso", "ansiedade" -> Triple("😰", Color(0xFF7E57C2), "Ansioso(a)")
        "enjoado" -> Triple("🤢", Color(0xFF66BB6A), "Indisposto(a)")
        "amoroso" -> Triple("💖", Color(0xFFFF80AB), "Amoroso(a)")
        "neutro" -> Triple("😐", Color(0xFF9E9E9E), "Neutro")
        else -> Triple("🌿", Color(0xFF9E9E9E), mood.replaceFirstChar { it.uppercase() })
    }
}
