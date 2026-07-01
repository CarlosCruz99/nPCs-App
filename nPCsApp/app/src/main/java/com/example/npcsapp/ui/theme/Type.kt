package com.example.npcsapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.npcsapp.R

// Definición de Fuentes Variable
val Inter = FontFamily(
    Font(
        resId = R.font.inter_variable,
        // Al ser Variable Font, el sistema mapeará automáticamente los pesos
    )
)

val JetBrainsMono = FontFamily(
    Font(
        resId = R.font.jetbrains_mono_variable,
    )
)

// Configuración de la Tipografía para nPCs
val Typography = Typography(
    // Títulos grandes (equivalente a headline-xl de tu web)
    headlineLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.02).sp
    ),

    // Nombres de builds o componentes (headline-md)
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = (-0.01).sp
    ),

    // Texto de cuerpo (body-md)
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    // ETIQUETAS TÉCNICAS (Usa JetBrains Mono)
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.05.sp // Tracking tipo tech
    ),

    // Precios o Specs (Usa JetBrains Mono un poco más grande)
    labelMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    )
)