package com.smartsplit.app.presentation.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartsplit.app.domain.model.UserProfile
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.ui.components.*
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.util.LocalStrings

@Composable
fun EditProfileScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val strings   = LocalStrings.current
    val user      by viewModel.user.collectAsState()

    var nameInput by remember { mutableStateOf(user.name) }
    var photoUri  by remember { mutableStateOf<Uri?>(user.photoUri?.let { Uri.parse(it) }) }
    var nameError by remember { mutableStateOf("") }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> if (uri != null) photoUri = uri }

    Scaffold(
        topBar         = { SmartSplitTopBar(title = strings.editProfile, onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Avatar picker ────────────────────────────────────────
            Box(
                modifier         = Modifier.clickable { photoPicker.launch("image/*") },
                contentAlignment = Alignment.BottomEnd
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model              = photoUri,
                        contentDescription = "Profile photo",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(2.dp, Primary, CircleShape)
                    )
                } else {
                    Box(
                        modifier         = Modifier.size(96.dp).clip(CircleShape).background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            (nameInput.firstOrNull() ?: 'U').uppercaseChar().toString(),
                            color      = Color.White,
                            fontSize   = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier         = Modifier.size(30.dp).clip(CircleShape).background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CameraAlt, null,
                        tint     = Color.White,
                        modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { photoPicker.launch("image/*") }) {
                Text(strings.changePhoto, color = Primary, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(24.dp))

            // ── Name field ───────────────────────────────────────────
            FieldLabel(strings.name)
            OutlinedTextField(
                value         = nameInput,
                onValueChange = { nameInput = it; nameError = "" },
                singleLine    = true,
                isError       = nameError.isNotEmpty(),
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors(),
                leadingIcon   = {
                    Icon(Icons.Default.Person, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            )
            if (nameError.isNotEmpty()) FieldError(nameError)

            Spacer(Modifier.height(16.dp))

            // ── Email (read-only) ────────────────────────────────────
            FieldLabel(strings.email)
            OutlinedTextField(
                value         = user.email,
                onValueChange = {},
                readOnly      = true,
                enabled       = false,
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTextColor   = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                leadingIcon = {
                    Icon(Icons.Default.Email, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            )

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                text    = strings.saveChanges,
                onClick = {
                    nameError = if (nameInput.isBlank()) strings.fieldRequired else ""
                    if (nameError.isEmpty()) {
                        viewModel.updateUser(
                            UserProfile(
                                name     = nameInput.trim(),
                                email    = user.email,
                                photoUri = photoUri?.toString()
                            )
                        )
                        onBack()
                    }
                }
            )
        }
    }
}