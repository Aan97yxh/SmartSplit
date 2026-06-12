package com.smartsplit.app.presentation.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.ui.components.AuthTextField
import com.smartsplit.app.ui.components.PrimaryButton
import com.smartsplit.app.util.IndonesianStrings
import com.smartsplit.app.util.LocalStrings
import com.smartsplit.app.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val strings      = LocalStrings.current
    val context      = LocalContext.current
    val scope        = rememberCoroutineScope()
    var email        by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError   by remember { mutableStateOf("") }
    var passError    by remember { mutableStateOf("") }
    var isLoading    by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        var ok = true
        emailError = when {
            email.isBlank() -> { ok = false; strings.fieldRequired }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { ok = false; strings.invalidEmail }
            else -> ""
        }
        passError = if (password.isBlank()) { ok = false; strings.fieldRequired } else ""
        return ok
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            Image(
                painter            = painterResource(id = R.drawable.logoapk2),
                contentDescription = "Logo SmartSplit",
                modifier           = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = strings.login,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = strings.welcomeTo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            AuthTextField(
                value         = email,
                onValueChange = { email = it; emailError = "" },
                label         = strings.email,
                placeholder   = { Text(strings.emailPlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon   = { Icon(Icons.Outlined.Email, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardType  = KeyboardType.Email,
                errorText     = emailError
            )

            Spacer(Modifier.height(12.dp))

            AuthTextField(
                value                = password,
                onValueChange        = { password = it; passError = "" },
                label                = strings.password,
                placeholder          = { Text(strings.passwordPlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                leadingIcon          = { Icon(Icons.Outlined.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon         = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null, tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType         = KeyboardType.Password,
                errorText            = passError
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (strings == IndonesianStrings)
                                "Fitur reset kata sandi akan segera hadir!"
                            else
                                "Password reset feature is coming soon!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text(
                        text = strings.forgotPassword,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            PrimaryButton(
                text      = strings.login,
                isLoading = isLoading,
                onClick   = {
                    if (validate()) {
                        isLoading = true
                        scope.launch {
                            val success = viewModel.login(email.trim(), password)
                            isLoading = false
                            if (success) {
                                onLoginSuccess()
                            } else {
                                passError = if (strings == IndonesianStrings)
                                    "Email atau kata sandi salah" else "Wrong email or password"
                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))) {
                        append(strings.dontHaveAccount)
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(strings.register)
                    }
                })
            }
        }
    }
}