package com.florescer.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@Composable
fun SonsScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    val sons = listOf(
        "Som da Natureza" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        "Chuva Relaxante" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        "Floresta Tranquila" to "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
    )

    var currentPlaying by remember { mutableStateOf<String?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

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
                text = "Sons de Meditação 🎶",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Escolha um som para relaxar e meditar.",
                fontSize = 14.sp,
                color = RosaTexto.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            sons.forEach { (title, url) ->
                val isPlaying = currentPlaying == url

                Button(
                    onClick = {
                        if (isPlaying) {
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null
                            currentPlaying = null
                        } else {
                            mediaPlayer?.stop()
                            mediaPlayer?.release()

                            val newPlayer = MediaPlayer().apply {
                                setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                setDataSource(url)
                                prepareAsync()
                                setOnPreparedListener {
                                    start()
                                }
                            }

                            mediaPlayer = newPlayer
                            currentPlaying = url
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlaying) RosaTexto else RosaBotao
                    ),
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isPlaying) "Parar: $title" else "Ouvir: $title",
                        fontSize = 16.sp,
                        color = Branco,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    currentPlaying = null
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}
