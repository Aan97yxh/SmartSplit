package com.smartsplit.app.presentation.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.ui.components.PrimaryButton
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.util.LocalStrings

private data class OnboardingPage(
    val icon: ImageVector,
    val titleId: String,
    val descId: String
)

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    val strings = LocalStrings.current
    val pages = listOf(
        Triple(Icons.Default.Receipt,      "Catat Tagihan",       "Simpan semua tagihan restoran dengan mudah dan cepat."),
        Triple(Icons.Default.Group,        "Bagi Per Orang",      "Hitung otomatis tagihan masing-masing orang termasuk pajak dan servis."),
        Triple(Icons.Default.CheckCircle,  "Tandai Lunas",        "Pantau siapa saja yang sudah bayar langsung dari aplikasi.")
    )
    var current by remember { mutableStateOf(0) }
    val isLast  = current == pages.lastIndex

    Column(
        modifier            = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        // Icon
        Box(
            modifier         = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                pages[current].first,
                contentDescription = null,
                tint     = Primary,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            pages[current].second,
            fontSize   = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
            modifier   = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            pages[current].third,
            fontSize  = 15.sp,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier  = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(Modifier.weight(1f))

        // Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            pages.indices.forEach { i ->
                val color by animateColorAsState(
                    if (i == current) Primary else Primary.copy(alpha = 0.3f),
                    label = "dot"
                )
                Box(
                    modifier = Modifier
                        .size(if (i == current) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!isLast) {
                OutlinedButton(
                    onClick  = onDone,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Text("Lewati")
                }
            }
            PrimaryButton(
                text     = if (isLast) "Mulai" else "Lanjut",
                onClick  = { if (isLast) onDone() else current++ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}