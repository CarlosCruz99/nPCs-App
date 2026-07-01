package com.example.npcsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.npcsapp.ui.theme.NeonBlue
import com.example.npcsapp.ui.theme.OnPrimary
import com.example.npcsapp.ui.theme.OnSurfaceVariant
import com.example.npcsapp.ui.theme.OutlineVariant
import com.example.npcsapp.ui.theme.SurfaceContainerHigh

// ─────────────────────────────────────────────────────────────────────────
// FAB unificado  — idéntico en todas las pantallas
// ─────────────────────────────────────────────────────────────────────────

/**
 * Botón de acción flotante estándar de nPCs.
 * Usa NeonBlue como color de fondo en todas las pantallas.
 * [label] opcional: si se provee se muestra junto al ícono (+).
 */
@Composable
fun nPCsFab(
    onClick: () -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    if (label != null) {
        // FAB extendido
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(NeonBlue)
                .border(1.dp, NeonBlue.copy(alpha = 0.40f), RoundedCornerShape(16.dp))
                .clickable { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Center)
                    .also { } // necesario para que Row sea el contenido del Box
            ) {
                // El contenido va dentro, pero Box no es un RowScope, usamos Box aparte
            }
            // Re-hacemos con Row directamente dentro del Box via padding
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Center)
                    .also { }
            ) { }
        }
        // Implementación real como Button estilizado
        Button(
            onClick       = onClick,
            shape         = RoundedCornerShape(16.dp),
            colors        = ButtonDefaults.buttonColors(
                containerColor = NeonBlue,
                contentColor   = OnPrimary
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            modifier       = modifier
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp), tint = OnPrimary)
            Spacer(Modifier.width(8.dp))
            Text(
                text       = label.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize   = 13.sp,
                letterSpacing = 0.5.sp,
                color      = OnPrimary
            )
        }
    } else {
        // FAB circular
        Box(
            modifier = modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(NeonBlue)
                .border(1.dp, NeonBlue.copy(alpha = 0.30f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Acción principal",
                tint     = OnPrimary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────
// Botón primario estándar (relleno NeonBlue)
// ─────────────────────────────────────────────────────────────────────────

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick        = onClick,
        enabled        = enabled,
        shape          = RoundedCornerShape(10.dp),
        colors         = ButtonDefaults.buttonColors(
            containerColor         = NeonBlue,
            contentColor           = OnPrimary,
            disabledContainerColor = SurfaceContainerHigh,
            disabledContentColor   = OnSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier       = modifier
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────
// Botón secundario (contorno, sin relleno)
// ─────────────────────────────────────────────────────────────────────────

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick        = onClick,
        shape          = RoundedCornerShape(10.dp),
        colors         = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor   = OnSurfaceVariant
        ),
        border         = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier       = modifier
    ) {
        Text(text, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────
// Formato de precio unificado
// ─────────────────────────────────────────────────────────────────────────

/**
 * Formatea un Float como precio en USD con dos decimales.
 * Ejemplo: 550f → "$550.00"
 */
fun Float.toPrecio(): String = "${"$"}${"%.2f".format(this)}"

/**
 * Versión sin centavos para precios enteros grandes (ej. cards de comunidad).
 * Ejemplo: 2450f → "$2,450"
 */
fun Float.toPrecioCorto(): String {
    val entero = this.toInt()
    return "${"$"}${"%,d".format(entero)}"
}

// ─────────────────────────────────────────────────────────────────────────
// Texto de precio estilizado (reutilizable inline)
// ─────────────────────────────────────────────────────────────────────────

@Composable
fun PrecioTexto(
    precio: Float,
    fontSize: Int = 16,
    color: Color = NeonBlue,
    corto: Boolean = false
) {
    Text(
        text       = if (corto) precio.toPrecioCorto() else precio.toPrecio(),
        color      = color,
        fontSize   = fontSize.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
    )
}