package com.smartsplit.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartsplit.app.domain.model.Language
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.presentation.settings.SettingsViewModel
import com.smartsplit.app.ui.components.ConfirmDialog
import com.smartsplit.app.ui.components.SmartSplitTopBar
import com.smartsplit.app.util.LocalStrings
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val strings              = LocalStrings.current
    val context              = LocalContext.current
    val isDarkMode           by settingsViewModel.isDarkMode.collectAsState()
    val language             by settingsViewModel.language.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val user                 by authViewModel.user.collectAsState()

    var showLanguagePicker by remember { mutableStateOf(false) }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isSystemEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
                settingsViewModel.syncNotificationStatus(isSystemEnabled)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showLogoutDialog   by remember { mutableStateOf(false) }

    Scaffold(
        topBar         = { SmartSplitTopBar(title = strings.settings, onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Profile row ──────────────────────────────────────────
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable(onClick = onNavigateToProfile),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (user.photoUri != null) {
                        AsyncImage(
                            model              = user.photoUri,
                            contentDescription = null,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.size(50.dp).clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier         = Modifier.size(50.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (user.name.firstOrNull() ?: 'U').uppercaseChar().toString(),
                                color      = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(user.name,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                        Text(user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // ── Appearance ───────────────────────────────────────────
            SettingsGroup {
                SettingsToggleRow(
                    icon          = Icons.Default.DarkMode,
                    label         = strings.darkMode,
                    checked       = isDarkMode,
                    onToggle      = { settingsViewModel.toggleDarkMode() },
                    checkedIcon   = Icons.Default.DarkMode,
                    uncheckedIcon = Icons.Default.WbSunny
                )
                SettingsDivider()
                SettingsClickRow(
                    icon    = Icons.Default.Language,
                    label   = strings.language,
                    value   = language.label,
                    onClick = { showLanguagePicker = true }
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Notifications ────────────────────────────────────────
            SettingsGroup {
                SettingsToggleRow(
                    icon          = Icons.Default.Notifications,
                    label         = strings.notifications,
                    checked       = notificationsEnabled,
                    onToggle      = {
                        settingsViewModel.toggleNotifications()
                        val intent = android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    },
                    checkedIcon   = Icons.Default.Notifications,
                    uncheckedIcon = Icons.Default.NotificationsOff
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── About ────────────────────────────────────────────────
            SettingsGroup {
                SettingsClickRow(
                    icon    = Icons.Default.Info,
                    label   = strings.about,
                    value   = "${strings.version} 1.0.0",
                    onClick = {}
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Logout ───────────────────────────────────────────────
            SettingsGroup {
                SettingsClickRow(
                    icon       = Icons.AutoMirrored.Filled.ExitToApp,
                    label      = strings.logout,
                    value      = "",
                    iconTint   = MaterialTheme.colorScheme.error,
                    labelColor = MaterialTheme.colorScheme.error,
                    onClick    = { showLogoutDialog = true }
                )
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SmartSplit ${strings.version} 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(strings.madeWith,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // ── Language picker ──────────────────────────────────────────────
    if (showLanguagePicker) {
        AlertDialog(
            onDismissRequest = { showLanguagePicker = false },
            title = { Text(strings.language, fontWeight = FontWeight.SemiBold) },
            text  = {
                Column {
                    Language.values().forEach { lang ->
                        val selected = language == lang
                        Row(
                            modifier          = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    settingsViewModel.setLanguage(lang)
                                    showLanguagePicker = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(lang.label,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                            if (selected) Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // ── Logout confirm ───────────────────────────────────────────────
    if (showLogoutDialog) {
        ConfirmDialog(
            title       = strings.logout,
            message     = if (language == Language.INDONESIAN)
                "Yakin ingin keluar?" else "Are you sure you want to logout?",
            confirmText = strings.logout,
            dismissText = strings.cancel,
            onConfirm   = { authViewModel.logout(); onLogout() },
            onDismiss   = { showLogoutDialog = false }
        )
    }
}

// ── Helper composables ────────────────────────────────────────────

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(content = content)
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color    = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onToggle: () -> Unit,
    checkedIcon: ImageVector? = null,
    uncheckedIcon: ImageVector? = null
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(icon = icon, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))

        Switch(
            checked         = checked,
            onCheckedChange = { onToggle() },
            thumbContent = if (checkedIcon != null && uncheckedIcon != null) {
                {
                    Icon(
                        imageVector = if (checked) checkedIcon else uncheckedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            } else null,
            colors          = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                checkedIconColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun SettingsClickRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    iconTint: Color?  = null,
    labelColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val finalIconTint = iconTint ?: MaterialTheme.colorScheme.primary

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(icon = icon, tint = finalIconTint)
        Spacer(Modifier.width(14.dp))
        Text(label,
            style    = MaterialTheme.typography.bodyLarge,
            color    = labelColor,
            modifier = Modifier.weight(1f))
        if (value.isNotEmpty()) {
            Text(value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Default.ChevronRight, null,
                tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun SettingsIcon(icon: ImageVector, tint: Color) {
    Box(
        modifier         = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.10f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
    }
}