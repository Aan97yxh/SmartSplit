package com.smartsplit.app.presentation.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.ui.components.AuthTextField
import com.smartsplit.app.ui.components.PrimaryButton
import com.smartsplit.app.util.LocalStrings
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val strings         = LocalStrings.current
    val scope           = rememberCoroutineScope()
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPass        by remember { mutableStateOf(false) }
    var showConfirm     by remember { mutableStateOf(false) }
    var nameError       by remember { mutableStateOf("") }
    var emailError      by remember { mutableStateOf("") }
    var passError       by remember { mutableStateOf("") }
    var confirmError    by remember { mutableStateOf("") }

    fun validate(): Boolean {
        var ok = true
        nameError    = if (fullName.isBlank()) { ok = false; strings.fieldRequired } else ""
        emailError   = when {
            email.isBlank() -> { ok = false; strings.fieldRequired }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { ok = false; strings.invalidEmail }
            else -> ""
        }
        passError    = if (password.length < 6) { ok = false; strings.passwordTooShort } else ""
        confirmError = if (password != confirmPassword) { ok = false; strings.passwordMismatch } else ""
        return ok
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            Box(
                modifier         = Modifier.size(72.dp).clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("SS", color = MaterialTheme.colorScheme.primary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            // FIX KONTRAS JUDUL UTAMA REGISTER
            Text(
                text = strings.register,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = strings.createAccount,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // TAMBAH PARAMETER PLACEHOLDER DI TIAP FIELD
            AuthTextField(
                value = fullName, onValueChange = { fullName = it; nameError = "" },
                label = strings.fullName,
                placeholder = { Text(strings.namePlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                errorText = nameError
            )
            Spacer(Modifier.height(12.dp))

            // EMAIL FIELD
            AuthTextField(
                value = email, onValueChange = { email = it; emailError = "" },
                label = strings.email, keyboardType = KeyboardType.Email,
                placeholder = { Text(strings.emailPlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Email, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                errorText = emailError
            )
            Spacer(Modifier.height(12.dp))

            // PASSWORD FIELD
            AuthTextField(
                value = password, onValueChange = { password = it; passError = "" },
                label = strings.password,
                placeholder = { Text(strings.passwordMinPlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password, errorText = passError
            )
            Spacer(Modifier.height(12.dp))

            // CONFIRM PASSWORD FIELD
            AuthTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it; confirmError = "" },
                label = strings.confirmPassword,
                placeholder = { Text(strings.confirmPasswordPlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(if (showConfirm) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password, errorText = confirmError
            )

            Spacer(Modifier.height(24.dp))

            PrimaryButton(text = strings.register, onClick = {
                if (validate()) {
                    scope.launch {
                        val success = viewModel.register(fullName.trim(), email.trim(), password)
                        if (success) onRegisterSuccess()
                    }
                }
            })

            Spacer(Modifier.height(16.dp))

            // FIX WARNA LINK REGISTER
            TextButton(onClick = onNavigateToLogin) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))) {
                        append(strings.alreadyHaveAccount)
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(strings.login)
                    }
                })
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}