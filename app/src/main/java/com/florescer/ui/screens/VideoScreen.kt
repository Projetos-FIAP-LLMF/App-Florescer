package com.florescer.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideosScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val context = LocalContext.current

    val videos = listOf(
        "Respiração para aliviar ansiedade" to "https://www.youtube.com/embed/2OEL4P1Rz04",
        "Meditação guiada para relaxamento" to "https://www.youtube.com/embed/inpok4MKVLM",
        "Yoga suave para iniciantes" to "https://www.youtube.com/embed/v7AYKMP6rOE"
    )

    var selectedVideo by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Vídeos Relaxantes 🎥",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Escolha um vídeo para relaxar e cuidar de você.",
                fontSize = 14.sp,
                color = RosaTexto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            videos.forEach { (title, url) ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RosaBotao),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedVideo = url
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Branco,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            selectedVideo?.let { url ->
                Text(
                    text = "Visualizando vídeo selecionado:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AndroidView(
                        factory = {
                            WebView(it).apply {
                                settings.javaScriptEnabled = true
                                webViewClient = WebViewClient()
                                loadUrl(url)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
