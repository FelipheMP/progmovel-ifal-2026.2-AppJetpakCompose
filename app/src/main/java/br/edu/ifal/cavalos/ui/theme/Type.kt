package br.edu.ifal.cavalos.ui.theme

import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import br.edu.ifal.cavalos.R

// As fontes do AulaPRMO são variáveis: um arquivo contém vários pesos.
// Registramos cada peso e seu eixo real para evitar traços finos ou negrito artificial.
@OptIn(ExperimentalTextApi::class)
private fun familiaVariavel(recurso: Int): FontFamily {
    // Fontes variáveis exigem Android 8. Em Android 7, fonte do sistema.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return FontFamily.Default

    return FontFamily(
        Font(recurso, weight = FontWeight.Normal,
            variationSettings = FontVariation.Settings(FontVariation.weight(400))),
        Font(recurso, weight = FontWeight.Medium,
            variationSettings = FontVariation.Settings(FontVariation.weight(500))),
        Font(recurso, weight = FontWeight.SemiBold,
            variationSettings = FontVariation.Settings(FontVariation.weight(600)))
    )
}

private val titulos = familiaVariavel(R.font.inter)
private val subtitulos = familiaVariavel(R.font.manrope)
private val textos = familiaVariavel(R.font.roboto)

// Mantemos as medidas do Material 3 e escolhemos pesos legíveis para cada função.
private val padrao = Typography()
val TipografiaCavalos = Typography(
    headlineMedium = padrao.headlineMedium.copy(
        fontFamily = titulos, fontWeight = FontWeight.SemiBold),
    titleLarge = padrao.titleLarge.copy(
        fontFamily = subtitulos, fontWeight = FontWeight.SemiBold),
    titleMedium = padrao.titleMedium.copy(
        fontFamily = subtitulos, fontWeight = FontWeight.Medium),
    bodyLarge = padrao.bodyLarge.copy(
        fontFamily = textos, fontWeight = FontWeight.Normal),
    bodyMedium = padrao.bodyMedium.copy(
        fontFamily = textos, fontWeight = FontWeight.Normal),
    labelLarge = padrao.labelLarge.copy(
        fontFamily = textos, fontWeight = FontWeight.Medium),
    labelMedium = padrao.labelMedium.copy(
        fontFamily = textos, fontWeight = FontWeight.Medium),
    labelSmall = padrao.labelSmall.copy(
        fontFamily = textos, fontWeight = FontWeight.Medium)
)
