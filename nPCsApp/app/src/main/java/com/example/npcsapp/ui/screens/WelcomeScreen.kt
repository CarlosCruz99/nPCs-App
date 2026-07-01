package com.example.npcsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.npcsapp.R
import com.example.npcsapp.ui.theme.NeonBlue
import com.example.npcsapp.ui.theme.NeonCyan
import com.example.npcsapp.ui.theme.OnPrimary
import com.example.npcsapp.ui.theme.OnSurfaceVariant
import com.example.npcsapp.ui.theme.PrimaryContainer
import com.example.npcsapp.ui.theme.SurfaceCard
import com.example.npcsapp.ui.theme.SurfaceDeep


@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PrimaryContainer.copy(alpha = 0.25f),
                        SurfaceDeep
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // ── Logo ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .border(2.dp, NeonBlue.copy(alpha = 0.40f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "Logo nPCs",
                    tint = NeonBlue,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "nPCs",
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Arma tu PC ideal",
                color = OnSurfaceVariant.copy(alpha = 0.85f),
                fontSize = 15.sp
            )

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = onStart,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue,
                    contentColor = OnPrimary
                ),
                contentPadding = PaddingValues(horizontal = 56.dp, vertical = 16.dp)
            ) {
                Text("GO", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 2.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
            }
        }
    }
}
