package com.florescer.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.florescer.data.HumorRepository
import com.florescer.data.model.Video
import com.florescer.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideosScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository
) {
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))
    val context = LocalContext.current

    // humor pill (igual Afirmações)
    val (emoji, accent, moodNice) = moodUi(mood)

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var items by remember { mutableStateOf<List<VideoItem>>(emptyList()) }
    var selected by remember { mutableStateOf<VideoItem?>(null) }

    // lifecycle do WebView
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, selected) {
        val observer = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                webViewRef?.onPause()
            }

            override fun onResume(owner: LifecycleOwner) {
                webViewRef?.onResume()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                webViewRef?.destroy(); webViewRef = null
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Carrega vídeos com fallback e seleção inicial
    LaunchedEffect(Unit) {
        try {
            val videos = repository.getVideos(10)
            val mapped = videos.map { it.toVideoItem() }.filter { it.hasValidUrl }
            items = if (mapped.isNotEmpty()) mapped else getVideosFallback()
            if (selected == null && items.isNotEmpty()) selected = items.first()
        } catch (e: Exception) {
            e.printStackTrace()
            error = e.message ?: "Não foi possível carregar os vídeos."
            items = getVideosFallback()
            if (selected == null && items.isNotEmpty()) selected = items.first()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER com humor (igual Afirmações)
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
                    text = "Vídeos Relaxantes 🎥",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Escolha um vídeo curtinho pra desacelerar e se acolher no seu tempo.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        when {
            isLoading -> LoadingCard()
            error != null && items.isEmpty() -> ErrorCard(message = error!!)
            items.isEmpty() -> EmptyStateCard()

            else -> {
                // LISTA
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEach { item ->
                        VideoCard(
                            item = item,
                            isSelected = item == selected,
                            onClick = { selected = item }
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                // PLAYER
                selected?.let { item ->
                    Text(
                        text = "Reproduzindo: ${item.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RosaTexto,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            val ytId = item.videoId ?: extractYoutubeId(item.fullUrl)
                            if (!ytId.isNullOrBlank()) {
                                // Player via iframe (mais estável)
                                val html = remember(ytId) { buildYoutubeHtml(ytId) }
                                AndroidView(
                                    factory = { ctx ->
                                        WebView(ctx).apply {
                                            webViewClient = WebViewClient()
                                            webChromeClient = WebChromeClient()
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                            // Evita que precise dar play manual (opcional)
                                            settings.mediaPlaybackRequiresUserGesture = false
                                            // "limpa" antes de recarregar (evita ficar com o mesmo frame)
                                            loadUrl("about:blank")
                                            loadDataWithBaseURL(
                                                "https://www.youtube.com",
                                                html,
                                                "text/html",
                                                "utf-8",
                                                null
                                            )
                                            webViewRef = this
                                        }
                                    },
                                    update = { wv ->
                                        wv.loadUrl("about:blank")
                                        wv.loadDataWithBaseURL(
                                            "https://www.youtube.com",
                                            html,
                                            "text/html",
                                            "utf-8",
                                            null
                                        )
                                        webViewRef = wv
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (!item.fullUrl.isNullOrBlank()) {
                                // Qualquer URL — abre direto
                                AndroidView(
                                    factory = { ctx ->
                                        WebView(ctx).apply {
                                            webViewClient = WebViewClient()
                                            webChromeClient = WebChromeClient()
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                            loadUrl(item.fullUrl)
                                            webViewRef = this
                                        }
                                    },
                                    update = { it.loadUrl(item.fullUrl) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val externalUrl = item.videoUrlForExternal
                                if (externalUrl.isNotBlank()) {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl))
                                    )
                                }
                            },
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
                        ) {
                            Text("Abrir no app", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                val idx = items.indexOfFirst { it == selected }
                                val next = items.getOrNull((idx + 1) % items.size)
                                if (next != null) selected = next
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Próximo vídeo",
                                color = Branco,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // VOLTAR
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

/* ==================== MODELOS AUXILIARES ==================== */

private data class VideoItem(
    val title: String,
    val videoId: String?,   // se for YouTube
    val fullUrl: String?    // qualquer URL fallback
) {
    val hasValidUrl: Boolean get() = videoId != null || !fullUrl.isNullOrBlank()

    val thumbUrl: String?
        get() = videoId?.let { "https://i.ytimg.com/vi/$it/hqdefault.jpg" }

    val videoUrlForExternal: String
        get() = videoId?.let { "https://www.youtube.com/watch?v=$it" } ?: (fullUrl ?: "")
}

private fun Video.toVideoItem(): VideoItem {
    val ytId = extractYoutubeId(url)
    return VideoItem(
        title = title.ifBlank { "Vídeo relaxante" },
        videoId = ytId,
        fullUrl = if (ytId == null) url else null
    )
}

/* ==================== FALLBACK PADRÃO ==================== */

private fun getVideosFallback(): List<VideoItem> = listOf(
    VideoItem("Exercício de respiração (5 min)", "ZToicYcHIOU", null),
    VideoItem("Meditação guiada para relaxamento", "inpok4MKVLM", null),
    VideoItem("Yoga suave para iniciantes", "v7AYKMP6rOE", null)
)

/* ==================== UI HELPERS ==================== */

@Composable
private fun LoadingCard() {
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
            Text("Carregando vídeos…", color = RosaTexto, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
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
            Text("Sem vídeos por agora", fontWeight = FontWeight.SemiBold, color = RosaTexto)
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

@Composable
private fun VideoCard(
    item: VideoItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val thumb = item.thumbUrl
            if (thumb != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumb)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF7E6EE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Vídeo",
                        color = RosaTexto,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    item.title,
                    color = RosaTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (isSelected) "Reproduzindo agora" else "Toque para assistir",
                    color = if (isSelected) RosaBotao else Preto.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

/* ==================== HELPERS ==================== */

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

fun extractYoutubeId(url: String?): String? {
    if (url.isNullOrBlank()) return null
    val regexes = listOf(
        "(?<=v=)[A-Za-z0-9_-]{11}",
        "(?<=be/)[A-Za-z0-9_-]{11}",
        "(?<=embed/)[A-Za-z0-9_-]{11}"
    )
    for (r in regexes) {
        Regex(r).find(url)?.value?.let { return it }
    }
    return null
}

private fun buildYoutubeHtml(videoId: String): String = """
    <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          html,body { margin:0; background:#000; height:100%; }
          .wrap { position:fixed; inset:0; }
          iframe { width:100%; height:100%; border:0; }
        </style>
      </head>
      <body>
        <div class="wrap">
          <iframe 
            src="https://www.youtube.com/embed/$videoId?rel=0&modestbranding=1&playsinline=1"
            frameborder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            allowfullscreen>
          </iframe>
        </div>
      </body>
    </html>
""".trimIndent()
