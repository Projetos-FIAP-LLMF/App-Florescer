package com.florescer.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.data.HumorRepository
import com.florescer.data.model.Som
import com.florescer.ui.theme.*

/* ================== TELA ================== */

@Composable
fun SonsScreen(
    navController: NavHostController,
    humorRepository: HumorRepository,
    mood: String? = null
) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    // Estado do player
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentUrl by remember { mutableStateOf<String?>(null) }
    var isPreparing by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Lista de sons (API + fallback)
    var isLoading by remember { mutableStateOf(true) }
    var items by remember { mutableStateOf<List<SoundItem>>(emptyList()) }

    // Header mood (se vier)
    val moodUi = remember(mood) { mood?.let { moodUi(it) } }

    // Carregamento
    LaunchedEffect(Unit) {
        try {
            val apiItems = humorRepository
                .getSons(limit = 10)
                .mapNotNull { it.toSoundItemOrNull() }
            items = if (apiItems.isNotEmpty()) apiItems else getLocalSoundsFallback()
        } catch (e: Exception) {
            e.printStackTrace()
            errorMsg = e.message ?: "Não foi possível carregar os sons."
            items = getLocalSoundsFallback()
        } finally {
            isLoading = false
        }
    }

    // Liberar o player ao sair
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // Controles do player
    fun stopAndRelease() {
        runCatching { mediaPlayer?.stop() }
        mediaPlayer?.release()
        mediaPlayer = null
        currentUrl = null
        isPreparing = false
    }

    fun play(url: String) {
        errorMsg = null
        if (currentUrl == url && mediaPlayer != null) {
            // já tocando este => parar
            stopAndRelease()
            return
        }
        stopAndRelease()
        isPreparing = true

        val mp = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            setOnPreparedListener {
                isPreparing = false
                start()
            }
            setOnCompletionListener { stopAndRelease() }
            setOnErrorListener { _, _, _ ->
                errorMsg = "Não foi possível reproduzir este áudio."
                stopAndRelease()
                true
            }
            prepareAsync()
        }
        mediaPlayer = mp
        currentUrl = url
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER (no padrão das outras telas)
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
                // Pill de humor (se informado)
                moodUi?.let { (emoji, accent, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(accent.copy(alpha = 0.12f), RoundedCornerShape(100))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(text = emoji, fontSize = 18.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = label,
                            color = accent,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Text(
                    text = "Sons de Meditação 🎶",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Escolha um som para relaxar, respirar e acalmar o coração.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        when {
            isLoading -> LoadingCard(text = "Carregando sons…")

            else -> {
                errorMsg?.let {
                    ErrorCard(message = it)
                    Spacer(Modifier.height(12.dp))
                }

                if (items.isEmpty()) {
                    EmptyStateCard()
                } else {
                    // “Player grande” do item atual (com disco girando)
                    val current = items.firstOrNull { it.url == currentUrl }
                    BigNowPlayingCard(
                        title = current?.title ?: "Nada tocando",
                        isPlaying = mediaPlayer != null && !isPreparing,
                        isPreparing = isPreparing
                    )

                    Spacer(Modifier.height(14.dp))

                    // Lista de faixas
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items.forEach { item ->
                            val isThisPreparing = isPreparing && currentUrl == item.url
                            val isPlaying =
                                currentUrl == item.url && mediaPlayer != null && !isPreparing

                            SoundRow(
                                title = item.title,
                                isPlaying = isPlaying,
                                isPreparing = isThisPreparing,
                                onPlayPause = { play(item.url) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Ações inferiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { stopAndRelease() },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
            ) {
                Text("Parar tudo", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = {
                    stopAndRelease()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Voltar", color = Branco, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/* ================== COMPONENTES ================== */

@Composable
private fun BigNowPlayingCard(
    title: String,
    isPlaying: Boolean,
    isPreparing: Boolean
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Disco “girando” (só anima quando estiver tocando)
            val rotation by rememberInfiniteTransition(label = "disc")
                .animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 6000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "disc-anim"
                )

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color(0xFFFDF0F5), CircleShape)
                    .border(2.dp, RosaBotao.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(RosaBotao.copy(alpha = 0.15f), CircleShape)
                        .border(2.dp, RosaBotao.copy(alpha = 0.35f), CircleShape)
                        .rotate(if (isPlaying) rotation else 0f)
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(RosaBotao, CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = when {
                    isPreparing -> "Carregando…"
                    isPlaying -> "Tocando agora"
                    else -> "Nada tocando"
                },
                fontSize = 12.sp,
                color = if (isPlaying) RosaBotao else Preto.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SoundRow(
    title: String,
    isPlaying: Boolean,
    isPreparing: Boolean,
    onPlayPause: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 3.dp else 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatarzinho do som
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(RosaBotao.copy(alpha = 0.12f), CircleShape)
                    .border(1.dp, RosaBotao.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(RosaBotao, CircleShape)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = RosaTexto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(
                    text = when {
                        isPreparing -> "Carregando…"
                        isPlaying -> "Tocando agora"
                        else -> "Toque para ouvir"
                    },
                    color = if (isPlaying) RosaBotao else Preto.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            val btnText = when {
                isPreparing -> "Carregando…"
                isPlaying -> "Parar"
                else -> "Ouvir"
            }
            Button(
                onClick = onPlayPause,
                enabled = !isPreparing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlaying) RosaTexto else RosaBotao
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(btnText, color = Branco, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
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
private fun ErrorCard(message: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ops! Algo deu errado", color = RosaTexto, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                message,
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sem sons por agora", fontWeight = FontWeight.SemiBold, color = RosaTexto)
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

/* ================== MODELOS / HELPERS (NÍVEL DE ARQUIVO) ================== */

private data class SoundItem(
    val title: String,
    val url: String
)

private fun Som.toSoundItemOrNull(): SoundItem? {
    // Tenta pegar title/titulo/nome; ajuste se seu modelo mudar
    val titleSafe =
        runCatching { this.nome }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: runCatching {
                this::class.java.getDeclaredField("titulo").apply { isAccessible = true }
                    .get(this) as? String
            }.getOrNull()?.takeIf { !it.isNullOrBlank() }
            ?: runCatching { this.nome }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: "Som relaxante"

    val urlSafe = runCatching { this.url }.getOrNull()?.takeIf { it.isNotBlank() } ?: return null
    return SoundItem(title = titleSafe, url = urlSafe)
}

private fun getLocalSoundsFallback(): List<SoundItem> = listOf(
    SoundItem("Som da natureza", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
    SoundItem("Chuva relaxante", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
    SoundItem("Floresta tranquila", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3")
)

/** Retorna (emoji, cor, rótulo) — mesma ideia usada nas outras telas */
private fun moodUi(mood: String): Triple<String, Color, String> =
    when (mood.lowercase()) {
        "feliz" -> Triple("😊", Color(0xFFFFC107), "Feliz")
        "triste" -> Triple("😢", Color(0xFF2196F3), "Triste")
        "bravo" -> Triple("😠", Color(0xFFF44336), "Bravo")
        "ansioso", "ansiedade" -> Triple("😰", Color(0xFF7E57C2), "Ansioso(a)")
        "enjoado" -> Triple("🤢", Color(0xFF66BB6A), "Indisposto(a)")
        "amoroso" -> Triple("💖", Color(0xFFFF80AB), "Amoroso(a)")
        "neutro" -> Triple("😐", Color(0xFF9E9E9E), "Neutro")
        else -> Triple("🌿", Color(0xFF9E9E9E), mood.replaceFirstChar { it.uppercase() })
    }
