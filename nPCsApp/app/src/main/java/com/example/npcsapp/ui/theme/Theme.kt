package com.example.npcsapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary            = NeonBlue,
    onPrimary          = OnPrimary,
    primaryContainer   = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary            = NeonCyan,
    onSecondary          = OnSecondary,
    secondaryContainer   = SecondaryContainer,
    onSecondaryContainer = OnSecondary,

    tertiary   = TertiaryAccent,
    onTertiary = OnPrimary,

    background   = SurfaceDeep,
    onBackground = OnBackground,

    surface            = SurfaceBase,
    onSurface          = OnSurface,
    surfaceVariant     = SurfaceVariant,
    onSurfaceVariant   = OnSurfaceVariant,
    surfaceContainerLow     = SurfaceContainerLow,
    surfaceContainer        = SurfaceContainer,
    surfaceContainerHigh    = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,

    outline        = Outline,
    outlineVariant = OutlineVariant,

    error   = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

// LightColorScheme se mantiene por si acaso, pero ya no se utiliza en el Theme global
private val LightColorScheme = lightColorScheme(
    primary      = Purple40,
    secondary    = PurpleGrey40,
    tertiary     = Pink40,
    background   = Color(0xFFFFFBFE),
    surface      = Color(0xFFFFFBFE),
    onPrimary    = Color.White,
    onSecondary  = Color.White,
    onTertiary   = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface    = Color(0xFF1C1B1F),
)

@Composable
fun NPCsAppTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Forzamos el uso de DarkColorScheme para que la aplicación sea siempre oscura
    // y evitar discrepancias visuales con el sistema.
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
