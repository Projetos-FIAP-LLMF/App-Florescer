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
import com.florescer.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.florescer.R
import com.florescer.ui.auth.AuthViewModel
import androidx.navigation.NavHostController
import com.florescer.ui.auth.AuthUiState

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val uiState by authViewModel.uiState.collectAsState()
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    // quando gerar token com sucesso, navega
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Sucesso) {
            navController.navigate("mood") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.titulo),
                contentDescription = "Logo do Florescer",
                modifier = Modifier
                    .width(400.dp)
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "A Florescer é uma pausa no caos.\n" +
                        "Um espaço de acolhimento emocional, onde você desacelera, se ouve e se cuida.\n" +
                        "Aqui, cada pessoa é respeitada no seu tempo — e cuidada com afeto.\n",
                fontSize = 15.sp,
                color = Preto,
                textAlign = TextAlign.Center
            )

            Text(
                text = "O que você já fez por você hoje?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            // renderização de acordo com o estado do auth
            when (val state = uiState) {
                is AuthUiState.Idle -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { authViewModel.onComecarClicked() },
                            colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Começar", color = Branco, fontSize = 20.sp)
                        }
                    }
                }

                is AuthUiState.Carregando -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = RosaTexto)
                        Text(
                            text = "Gerando token...",
                            color = RosaTexto,
                            fontSize = 14.sp
                        )
                    }
                }

                is AuthUiState.Sucesso -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = RosaTexto)
                        Text(
                            text = "Redirecionando...",
                            color = RosaTexto,
                            fontSize = 14.sp
                        )
                    }
                }

                is AuthUiState.Erro -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Erro: ${state.mensagem}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { authViewModel.onComecarClicked() },
                            colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Tentar Novamente", color = Branco, fontSize = 18.sp)
                        }
                    }
                }
            }

            Text(
                text = "Ao clicar no botão você não é identificado no processo de utilização do aplicativo.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Preto
            )
        }
    }
}
