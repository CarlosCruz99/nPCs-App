package com.example.npcsapp.ui.theme

import androidx.compose.ui.graphics.Color

// === nPCs Design System Colors ===
// Primary palette
val NeonBlue        = Color(0xFFADC6FF)   // primary
val NeonCyan        = Color(0xFF4CD7F6)   // secondary

// Surface / Background
val SurfaceDeep     = Color(0xFF121212)   // surface-deep / background
val SurfaceBase     = Color(0xFF131313)   // surface
val SurfaceCard     = Color(0xFF1E1E2E)   // surface-card (glass card base)
val SurfaceContainerLow     = Color(0xFF1C1B1B)
val SurfaceContainer        = Color(0xFF201F1F)
val SurfaceContainerHigh    = Color(0xFF2A2A2A)
val SurfaceContainerHighest = Color(0xFF353534)
val SurfaceVariant  = Color(0xFF353534)
val SurfaceBright   = Color(0xFF393939)

// On-surface
val OnSurface         = Color(0xFFE5E2E1)
val OnSurfaceVariant  = Color(0xFFC2C6D6)
val OnBackground      = Color(0xFFE5E2E1)

// Primary / containers
val PrimaryContainer     = Color(0xFF4D8EFF)
val OnPrimaryContainer   = Color(0xFF00285D)
val OnPrimary            = Color(0xFF002E6A)
val InversePrimary       = Color(0xFF005AC2)

// Secondary
val SecondaryContainer   = Color(0xFF03B5D3)
val OnSecondary          = Color(0xFF003640)

// Tertiary (accent orange)
val TertiaryAccent       = Color(0xFFFFB786)
val TertiaryContainer    = Color(0xFFDF7412)

// Status
val StatusNew    = Color(0xFF10B981)  // green  - "Nuevo"
val StatusOffer  = Color(0xFFEC4899)  // pink   - "Oferta"
val StatusUsed   = Color(0xFFF59E0B)  // amber  - "Usado / Limited"

// Outline
val Outline        = Color(0xFF8C909F)
val OutlineVariant = Color(0xFF424754)

// Glass overlay helper (for backgrounds using alpha)
val GlassBg        = Color(0xFF1E1E2E)   // use with .copy(alpha = 0.8f)
val WhiteOverlay   = Color.White         // use with .copy(alpha = 0.10f)

// ── Legacy aliases kept so existing code doesn't break ─────────────────────
val DeepBlueDark       = SurfaceDeep
val DeepBlueSurface    = SurfaceCard
val ElectricBlue       = PrimaryContainer
val CoolBlue           = NeonBlue
val DarkGradientStart  = SurfaceContainerLow
val DarkGradientEnd    = SurfaceDeep

val Purple80       = Color(0xFFD0BCFF)
val PurpleGrey80   = Color(0xFFCCC2DC)
val Pink80         = Color(0xFFEFB8C8)
val Purple40       = Color(0xFF6650A4)
val PurpleGrey40   = Color(0xFF625B71)
val Pink40         = Color(0xFF7D5260)