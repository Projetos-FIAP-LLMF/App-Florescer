package com.florescer.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florescer.data.AuthRepository

@Composable
fun AuthScreen(
    authRepository: AuthRepository,
    onTokenObtido: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Se já tem token, navega automaticamente
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Sucesso) {
            onTokenObtido()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is AuthUiState.Idle -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Bem-vindo ao Florescer",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Button(onClick = { viewModel.onComecarClicked() }) {
                        Text("Começar")
                    }
                }
            }
            is AuthUiState.Carregando -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Gerando token...")
                }
            }
            is AuthUiState.Sucesso -> {
                // Navegação automática via LaunchedEffect
                CircularProgressIndicator()
            }
            is AuthUiState.Erro -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Erro: ${state.mensagem}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.onComecarClicked() }) {
                        Text("Tentar Novamente")
                    }
                }
            }
        }
    }
}