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
    data class Sucesso(val token: String) : AuthUiState()
    data class Erro(val mensagem: String) : AuthUiState()
}

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        checkExistingToken()
    }

    private fun checkExistingToken() {
        viewModelScope.launch {
            val hasToken = authRepository.hasToken()
            if (hasToken) {
                val token = authRepository.getStoredToken()
                _uiState.value = AuthUiState.Sucesso(token ?: "")
            }
        }
    }

    fun onComecarClicked() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Carregando

            val result = authRepository.generateAndSaveToken()

            _uiState.value = if (result.isSuccess) {
                AuthUiState.Sucesso(result.getOrNull() ?: "")
            } else {
                AuthUiState.Erro(result.exceptionOrNull()?.message ?: "Erro desconhecido")
            }
        }
    }

    fun getToken(): String? {
        val state = _uiState.value
        return if (state is AuthUiState.Sucesso) state.token else null
    }
}