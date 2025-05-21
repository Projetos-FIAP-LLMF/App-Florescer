package com.florescer.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RosaBotao,
    onPrimary = Branco,
    background = GradienteTop,
    onBackground = Preto,
)

@Composable
fun FlorescerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(), // ou crie seu próprio Typography.kt
        content = content
    )
}

