package com.smartsplit.app.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.R
import com.smartsplit.app.ui.theme.Primary
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(700),
        label         = "splashAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(1500)
        onFinished()
    }

    Box(
        modifier         = Modifier.fillMaxSize().background(Color(0xFF33A8A6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier.alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter            = painterResource(id = R.drawable.logoapk2),
                contentDescription = "Logo SmartSplit",
                modifier           = Modifier.size(180.dp),
                contentScale       = ContentScale.Fit
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "SmartSplit",
                color      = Color.White,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Split bills, not friendships",
                color    = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }
    }
}