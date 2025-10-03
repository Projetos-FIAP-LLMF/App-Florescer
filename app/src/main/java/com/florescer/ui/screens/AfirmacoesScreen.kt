package com.florescer.ui.screens

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
import com.florescer.data.repository.HumorRepository
import com.florescer.data.model.Afirmacao
import com.florescer.ui.theme.*


@Composable
fun AfirmacoesScreen(
    navController: NavHostController,
    mood: String,
    repository: HumorRepository
) {
    var afirmacoes by remember { mutableStateOf<List<Afirmacao>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(mood) {
        try {
            afirmacoes = repository.getAfirmacoesPorHumor(mood)
        } catch (e: Exception) {
            error = e.message ?: "Erro ao carregar afirmações"
        } finally {
            isLoading = false
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Afirmações para você 🌿",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = RosaTexto,
                        modifier = Modifier.size(48.dp)
                    )
                }
                error != null -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = RosaBotao
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = error!!,
                            color = RosaTexto,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                afirmacoes.isEmpty() -> {
                    Text(
                        text = "Nenhuma afirmação disponível no momento.",
                        color = RosaTexto,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    afirmacoes.forEach { afirmacao ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = RosaBotao),
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 80.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = afirmacao.text,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Branco,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
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