package com.smartsplit.app.presentation.bill

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smartsplit.app.domain.model.BillDraft
import com.smartsplit.app.ui.components.*
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.util.LocalStrings
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import com.smartsplit.app.presentation.bill.BillViewModel

@Composable
fun SetupBillScreen(
    viewModel: BillViewModel,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val savedDraft by viewModel.draft.collectAsState()
    var restaurantName  by rememberSaveable { mutableStateOf("") }
    var date            by rememberSaveable {
        mutableStateOf(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date()))
    }
    var taxPercent      by rememberSaveable { mutableStateOf("10") }
    var servicePercent  by rememberSaveable { mutableStateOf("0") }
    var personInput     by rememberSaveable { mutableStateOf("") }
    var persons         by rememberSaveable { mutableStateOf(savedDraft?.persons ?: emptyList<String>()) }

    var nameError       by rememberSaveable { mutableStateOf("") }
    var personError     by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(savedDraft) {
        savedDraft?.let { draft ->
            restaurantName = draft.restaurantName
            date = draft.date
            taxPercent = if (draft.taxPercent % 1f == 0f) draft.taxPercent.toInt().toString() else draft.taxPercent.toString()
            servicePercent = if (draft.servicePercent % 1f == 0f) draft.servicePercent.toInt().toString() else draft.servicePercent.toString()

            if (persons.isEmpty()) {
                persons = draft.persons
            }
        }
    }

    fun validate(): Boolean {
        nameError   = if (restaurantName.isBlank()) strings.fieldRequired else ""
        personError = if (persons.isEmpty()) strings.atLeastOnePerson else ""
        return nameError.isEmpty() && personError.isEmpty()
    }

    Scaffold(
        topBar         = { SmartSplitTopBar(title = strings.setupBill, onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            FieldLabel(strings.restaurantName)
            OutlinedTextField(
                value         = restaurantName,
                onValueChange = { restaurantName = it; nameError = "" },
                singleLine    = true,
                isError       = nameError.isNotEmpty(),
                placeholder   = { Text(strings.restaurantNamePlaceholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors(),
                leadingIcon   = { Icon(Icons.Default.Restaurant, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant) }
            )
            if (nameError.isNotEmpty()) FieldError(nameError)

            Spacer(Modifier.height(14.dp))

            FieldLabel(strings.date)
            OutlinedTextField(
                value         = date,
                onValueChange = { date = it },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors(),
                leadingIcon   = { Icon(Icons.Default.CalendarToday, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant) }
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    FieldLabel(strings.taxPercent)
                    OutlinedTextField(
                        value         = taxPercent,
                        onValueChange = { v -> taxPercent = v.filter { it.isDigit() || it == '.' } },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = fieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon   = { Icon(Icons.Default.Percent, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                    )
                }
                Column(Modifier.weight(1f)) {
                    FieldLabel(strings.serviceChargePercent)
                    OutlinedTextField(
                        value         = servicePercent,
                        onValueChange = { v -> servicePercent = v.filter { it.isDigit() || it == '.' } },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = fieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon   = { Icon(Icons.Default.Percent, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            FieldLabel(strings.addPerson)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value         = personInput,
                    onValueChange = { personInput = it; personError = "" },
                    placeholder   = { Text(strings.personName) },
                    singleLine    = true,
                    modifier      = Modifier.weight(1f),
                    shape         = RoundedCornerShape(12.dp),
                    colors        = fieldColors()
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        val n = personInput.trim()
                        if (n.isNotBlank() && !persons.contains(n)) {
                            persons = persons + n; personInput = ""; personError = ""
                        }
                    },
                    shape          = RoundedCornerShape(12.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier       = Modifier.size(52.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add person")
                }
            }
            if (personError.isNotEmpty()) FieldError(personError)

            Spacer(Modifier.height(10.dp))

            if (persons.isNotEmpty()) {
                PersonChipGrid(persons = persons, onRemove = { persons = persons - it })
            }

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                text    = strings.continueToItems,
                onClick = {
                    if (validate()) {
                        viewModel.startDraft(
                            BillDraft(
                                restaurantName = restaurantName.trim(),
                                date           = date,
                                taxPercent     = taxPercent.toFloatOrNull() ?: 10f,
                                servicePercent = servicePercent.toFloatOrNull() ?: 0f,
                                persons        = persons
                            )
                        )
                        onContinue()
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}