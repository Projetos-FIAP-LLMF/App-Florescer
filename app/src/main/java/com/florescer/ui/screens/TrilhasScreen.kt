package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@Composable
fun TrilhasScreen(navController: NavHostController, mood: String) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))
    val (emoji, accent, moodNice) = remember(mood) { moodUi(mood) }

    val trilhas = listOf(
        TrilhaItem("✨ Afirmações", "afirmacoes/$mood", "Frases curtas para elevar seu dia."),
        TrilhaItem("🎥 Vídeos Relaxantes", "videos", "Clipes suaves para acalmar corpo e mente."),
        TrilhaItem("🎶 Sons de Meditação", "sons", "Áudios leves para respirar e desacelerar.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER — mood pill + título
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

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Escolha sua trilha de bem-estar 🌸",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Conteúdos curtinhos e carinhosos, no seu tempo.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // LISTA DE TRILHAS — cards fofos
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            trilhas.forEach { item ->
                TrilhaCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    accent = accent,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // Ações extras
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Se quiser, você também pode:",
                    color = RosaTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                // Ver meus dados
                FilledTonalButton(
                    onClick = { navController.navigate("evolucao") },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = accent.copy(alpha = 0.12f),
                        contentColor = RosaTexto
                    )
                ) {
                    Text("Ver meus Dados", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                // Voltar
                OutlinedButton(
                    onClick = { navController.navigate("mood") },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
                ) {
                    Text("Voltar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/* ---------- COMPONENTES ---------- */

@Composable
private fun TrilhaCard(
    title: String,
    subtitle: String,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar/ícone circular com cor de destaque
            Surface(color = accent.copy(alpha = 0.18f), shape = CircleShape) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // bolinha rosa fixa para identidade do app
                    Surface(color = RosaBotao, shape = CircleShape) {
                        Box(modifier = Modifier.size(12.dp))
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    color = RosaTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = subtitle,
                    color = Preto.copy(alpha = 0.75f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            AssistChip(
                onClick = onClick,
                label = { Text("Abrir", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
//                border = AssistChipDefaults.assistChipBorder(borderColor = accent.copy(alpha = 0.35f)),
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = RosaTexto,
                    containerColor = Color.White
                )
            )
        }
    }
}

/* ---------- MODELOS / HELPERS ---------- */

private data class TrilhaItem(
    val title: String,
    val route: String,
    val subtitle: String
)

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
