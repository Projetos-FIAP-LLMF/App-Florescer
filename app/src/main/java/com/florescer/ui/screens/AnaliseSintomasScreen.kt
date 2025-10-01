package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.florescer.data.model.Humor
import com.florescer.data.model.Recomendacao
import com.florescer.data.model.SintomasEntry
import com.florescer.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.florescer.data.AuthRepository

@Composable
fun AnaliseSintomasScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository,
    authRepository : AuthRepository
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val moodGif = when (mood.lowercase()) {
        "feliz" -> R.drawable.happycat
        "triste" -> R.drawable.sad
        "bravo" -> R.drawable.angry
        "ansioso", "ansiedade", "enjoado" -> R.drawable.anxious
        "neutro" -> R.drawable.neutral
        "amoroso" -> R.drawable.love
        else -> R.drawable.neutral
    }
    var token by remember { mutableStateOf<String?>(null) }
    var recomendacoes by remember { mutableStateOf<List<Recomendacao>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        token = authRepository.getStoredToken() ?: "default-token"
    }

    LaunchedEffect(token,mood) {
        if (token == null) return@LaunchedEffect
        isLoading = true
        error = null
        try {
            recomendacoes = withContext(Dispatchers.IO) {
                repository.getRecomendacoes(userId = token!!, limit = 5)
            }
        } catch (e: Exception) {
            error = e.message ?: "Erro desconhecido"
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(moodGif)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = "Seu humor do dia em gif de gatinho",
                modifier = Modifier.size(200.dp)
            )

            Text(
                "Analisamos como você está hoje…",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            if (isLoading) {
                CircularProgressIndicator(color = RosaTexto)
            } else if (error != null) {
                Text(error ?: "Erro", color = RosaTexto)
            } else {
                Text(
                    "Recomendações:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Preto,
                    textAlign = TextAlign.Center
                )

                recomendacoes.forEach { recomendacao ->
                    recomendacao.descricao?.let {
                        Text(
                            it,
                            fontSize = 14.sp,
                            color = Preto,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("trilhas/$mood") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Trilha de Bem-Estar", color = Branco, fontSize = 18.sp)
            }

            Button(
                onClick = { navController.navigate("avaliacao") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fazer Minha Autoavaliação", color = Branco, fontSize = 18.sp)
            }
        }
    }
}