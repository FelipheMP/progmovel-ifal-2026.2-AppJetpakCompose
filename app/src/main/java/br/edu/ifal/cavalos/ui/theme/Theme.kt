package br.edu.ifal.cavalos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CavalosTheme(
    temaEscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (temaEscuro) CoresEscuras else CoresClaras,
        // Os componentes Text recebem os estilos definidos em Type.kt.
        typography = TipografiaCavalos,
        content = content
    )
}
