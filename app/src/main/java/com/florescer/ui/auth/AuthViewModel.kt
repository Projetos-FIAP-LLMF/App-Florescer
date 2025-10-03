package com.florescer.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florescer.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Carregando : AuthUiState()
    data class Sucesso(val userId: String) : AuthUiState()
    data class Erro(val mensagem: String) : AuthUiState()
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onComecarClicked() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Carregando
            try {
                val userId = authRepository.getUserId()
                authRepository.registerDeviceInBackend()
                _uiState.value = AuthUiState.Sucesso(userId)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Erro(e.message ?: "Erro desconhecido")
            }
        }
    }


    fun getUserId(): String? {
        val state = _uiState.value
        return if (state is AuthUiState.Sucesso) state.userId else null
    }
}
