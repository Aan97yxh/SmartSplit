package com.smartsplit.app.presentation.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.ui.components.PrimaryButton
import com.smartsplit.app.ui.components.SmartSplitTopBar
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.util.LocalStrings

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onEditProfile: () -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val user    by authViewModel.user.collectAsState()

    Scaffold(
        topBar         = { SmartSplitTopBar(title = strings.profile, onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Avatar
            if (user.photoUri != null) {
                AsyncImage(
                    model              = user.photoUri,
                    contentDescription = null,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.size(96.dp).clip(CircleShape)
                )
            } else {
                Box(
                    modifier         = Modifier.size(96.dp).clip(CircleShape).background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        (user.name.firstOrNull() ?: 'U').uppercaseChar().toString(),
                        color      = Color.White,
                        fontSize   = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(user.name,
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(32.dp))

            // Info card
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoRow(
                        icon  = Icons.Default.Person,
                        label = strings.name,
                        value = user.name
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color    = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    ProfileInfoRow(
                        icon  = Icons.Default.Email,
                        label = strings.email,
                        value = user.email
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            PrimaryButton(text = strings.editProfile, onClick = onEditProfile)
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Primary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium)
        }
    }
}