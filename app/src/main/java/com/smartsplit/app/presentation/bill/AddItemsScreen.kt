package com.smartsplit.app.presentation.bill

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.domain.model.BillItem
import com.smartsplit.app.ui.components.*
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.util.LocalStrings
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.smartsplit.app.util.AppStrings
import com.smartsplit.app.util.NetworkMonitor

@Composable
fun AddItemsScreen(
    viewModel: BillViewModel,
    userEmail: String,
    onViewSummary: (billId: String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val draft   by viewModel.draft.collectAsState()
    val persons = draft?.persons ?: emptyList()

    val items        = remember { mutableStateListOf<BillItem>().also { list ->
        draft?.items?.let { list.addAll(it) }
    }}
    var showForm     by rememberSaveable { mutableStateOf(false) }
    var editingItem  by remember { mutableStateOf<BillItem?>(null) }
    var itemToDelete by remember { mutableStateOf<BillItem?>(null) }
    var summaryError by remember { mutableStateOf("") }

    LaunchedEffect(items.toList()) { viewModel.updateDraftItems(items.toList()) }

    Scaffold(
        topBar    = { SmartSplitTopBar(title = strings.addItems, onBack = onBack) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(strings.subtotal,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiah(items.sumOf { it.totalPrice }),
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                }
                if (summaryError.isNotEmpty()) {
                    Text(summaryError, color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
                Spacer(Modifier.height(8.dp))
                PrimaryButton(
                    text    = strings.viewSummary,
                    onClick = {
                        if (items.isEmpty()) summaryError = strings.atLeastOneItem
                        else {
                            summaryError = ""
                            val bill = viewModel.finalizeDraft(userEmail)
                            if (bill != null) onViewSummary(bill.id)
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            item {
                Text(
                    draft?.restaurantName ?: "",
                    style    = MaterialTheme.typography.bodySmall,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                )
            }

            if (items.isEmpty()) {
                item { EmptyItemsState(strings = strings) }
            } else {
                items(items = items, key = { it.id }) { item ->
                    AnimatedVisibility(
                        visible  = true,
                        enter    = fadeIn() + slideInVertically(),
                        modifier = Modifier.animateItem()
                    ) {
                        BillItemCard(
                            item        = item,
                            editLabel   = strings.edit,
                            deleteLabel = strings.delete,
                            onEdit      = { editingItem = item; showForm = true },
                            onDelete    = { itemToDelete = item }
                        )
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick  = { editingItem = null; showForm = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(52.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
                    Text(strings.addItem, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }

    if (showForm) {
        ItemFormSheet(
            persons     = persons,
            editingItem = editingItem,
            strings     = strings,
            viewModel   = viewModel,
            onDismiss   = { showForm = false; editingItem = null },
            onSave      = { newItem ->
                if (editingItem != null) {
                    val idx = items.indexOfFirst { it.id == editingItem!!.id }
                    if (idx >= 0) items[idx] = newItem.copy(id = editingItem!!.id)
                } else {
                    items.add(newItem)
                }
                showForm    = false
                editingItem = null
                summaryError = ""
            }
        )
    }

    itemToDelete?.let { item ->
        ConfirmDialog(
            title       = strings.deleteItem,
            message     = strings.deleteItemConfirm,
            confirmText = strings.delete,
            dismissText = strings.cancel,
            onConfirm   = { items.remove(item); itemToDelete = null },
            onDismiss   = { itemToDelete = null }
        )
    }
}

@Composable
private fun BillItemCard(
    item: BillItem,
    editLabel: String,
    deleteLabel: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(2.dp))
                    Text(item.assignedPersons.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (item.quantity > 1) {
                        Text("× ${item.quantity}",
                            style = MaterialTheme.typography.bodySmall, color = Primary)
                    }
                }
                Text(formatRupiah(item.totalPrice),
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(editLabel, fontSize = 12.sp)
                }

                Spacer(Modifier.width(6.dp))
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(deleteLabel, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun EmptyItemsState(strings: AppStrings) {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier.size(72.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.ShoppingBag, null,
                tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(strings.noItemsYet,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(strings.noItemsDesc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemFormSheet(
    persons: List<String>,
    editingItem: BillItem?,
    strings: AppStrings,
    viewModel: BillViewModel,
    onDismiss: () -> Unit,
    onSave: (BillItem) -> Unit
) {
    var itemName        by rememberSaveable { mutableStateOf(editingItem?.name ?: "") }
    var price           by rememberSaveable { mutableStateOf(editingItem?.unitPrice?.toString() ?: "") }
    var quantity        by rememberSaveable { mutableStateOf(editingItem?.quantity?.toString() ?: "1") }
    var selectedPersons by rememberSaveable {
        mutableStateOf(editingItem?.assignedPersons ?: emptyList<String>())
    }
    var nameError   by remember { mutableStateOf("") }
    var priceError  by remember { mutableStateOf("") }
    var personError by remember { mutableStateOf("") }

    // API
    val context = LocalContext.current
    val networkMonitor = remember { NetworkMonitor(context) }
    val isOnline by networkMonitor.isOnline.collectAsState(initial = true)

    val exchangeRate by viewModel.exchangeRate.collectAsState()
    val isCurrencyLoading by viewModel.isCurrencyLoading.collectAsState()

    var isJastipMode by rememberSaveable { mutableStateOf(false) }
    var foreignPrice by rememberSaveable { mutableStateOf("") }
    var selectedCurrency by rememberSaveable { mutableStateOf("USD") }
    val currencyOptions = listOf("USD", "SGD", "MYR", "EUR")
    var expandedDropdown by remember { mutableStateOf(false) }
    var cachedRate by rememberSaveable { mutableStateOf<Double?>(null) }

    LaunchedEffect(exchangeRate) {
        if (exchangeRate != null) {
            cachedRate = exchangeRate
        }
    }

    // Tembak API
    LaunchedEffect(isJastipMode, selectedCurrency) {
        if (isJastipMode) {
            cachedRate = null
            viewModel.fetchExchangeRate(selectedCurrency)
        } else {
            cachedRate = null
            price = editingItem?.unitPrice?.toString() ?: ""
            viewModel.clearExchangeRate()
        }
    }

    // Hitung otomatis
    LaunchedEffect(foreignPrice, cachedRate, isJastipMode) {
        val rate = cachedRate
        if (isJastipMode && rate != null) {
            val fPrice = foreignPrice.toDoubleOrNull()
            if (fPrice != null) {
                val calculatedIdr = (fPrice * rate).toLong()
                price = calculatedIdr.toString()
                priceError = ""
            } else {
                price = ""
            }
        }
    }

    // Matikan fitur jika offline
    LaunchedEffect(isOnline) {
        if (!isOnline) {
            isJastipMode = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor   = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(if (editingItem != null) strings.editItem else strings.addItem,
                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            FieldLabel(strings.itemName)
            OutlinedTextField(value = itemName,
                onValueChange = { itemName = it; nameError = "" },
                label = { Text(strings.itemName) },
                placeholder = {
                    Text(
                        text = strings.itemNamePlaceholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                singleLine = true, isError = nameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = fieldColors())
            if (nameError.isNotEmpty()) FieldError(nameError)
            Spacer(Modifier.height(12.dp))

            // ── MODUL KOMPONEN UI FITUR JASTIP (REMOTE API) ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(strings.jastipTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(
                        text = if (isOnline) strings.jastipOnline else strings.jastipOffline,
                        fontSize = 12.sp,
                        color = if (isOnline) Primary else Color.Gray
                    )
                }
                Switch(
                    checked = isJastipMode,
                    onCheckedChange = {
                        isJastipMode = it
                        if (!it) viewModel.clearExchangeRate()
                    },
                    enabled = isOnline
                )
            }

            if (isJastipMode) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { expandedDropdown = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("$selectedCurrency  ▼", fontSize = 13.sp)
                        }
                        DropdownMenu(expanded = expandedDropdown, onDismissRequest = { expandedDropdown = false }) {
                            currencyOptions.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        selectedCurrency = currency
                                        expandedDropdown = false
                                        if (foreignPrice.isNotEmpty()) viewModel.fetchExchangeRate(currency)
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = foreignPrice,
                        onValueChange = {
                            foreignPrice = it.filter { char -> char.isDigit() || char == '.' }
                        },
                        label = { Text("${strings.price} ($selectedCurrency)") },
                        singleLine = true,
                        modifier = Modifier.weight(2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (isCurrencyLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Primary)
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))


            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(2f)) {
                    FieldLabel(strings.price)
                    OutlinedTextField(
                        value = price,
                        onValueChange = {
                            if (!isJastipMode) {
                                price = it.filter { c -> c.isDigit() }
                                priceError = ""
                            }
                        },
                        readOnly = isJastipMode,
                        label = { Text(strings.price) },
                        prefix = {
                            Text("Rp ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        },
                        placeholder = {
                            Text(
                                text  = strings.pricePlaceholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        },
                        singleLine    = true,
                        isError       = priceError.isNotEmpty(),
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = fieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Column(Modifier.weight(1f)) {
                    FieldLabel(strings.quantity)
                    OutlinedTextField(
                        value         = quantity,
                        onValueChange = { q -> quantity = q.filter { it.isDigit() } },
                        placeholder   = { Text("1", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = fieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                        trailingIcon = {
                            Column(
                                modifier = Modifier.padding(end = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        val current = quantity.toIntOrNull() ?: 0
                                        quantity = (current + 1).toString()
                                    },
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Icon(Icons.Default.ArrowDropUp, contentDescription = "Tambah")
                                }

                                IconButton(
                                    onClick = {
                                        val current = quantity.toIntOrNull() ?: 1
                                        if (current > 1) {
                                            quantity = (current - 1).toString()
                                        } else {
                                            quantity = ""
                                        }
                                    },
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Kurang")
                                }
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            FieldLabel(strings.assignTo)
            val qty = quantity.toIntOrNull() ?: 1
            if (qty > 1 && selectedPersons.size == qty) {
                Surface(shape = RoundedCornerShape(8.dp), color = Primary.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(strings.quantityHint, fontSize = 11.sp, color = Primary,
                        modifier = Modifier.padding(8.dp))
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                persons.forEach { person ->
                    val selected = selectedPersons.contains(person)
                    FilterChip(
                        selected = selected,
                        onClick  = {
                            selectedPersons = if (selected) selectedPersons - person else selectedPersons + person
                            personError = ""
                        },
                        label  = { Text(person) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }
            if (personError.isNotEmpty()) FieldError(personError)
            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape    = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(strings.cancel)
                }
                Button(
                    onClick = {
                        nameError   = if (itemName.isBlank()) strings.fieldRequired else ""
                        priceError  = if (price.isBlank()) strings.fieldRequired else ""
                        personError = if (selectedPersons.isEmpty()) strings.atLeastOnePersonAssigned else ""
                        if (nameError.isEmpty() && priceError.isEmpty() && personError.isEmpty()) {
                            onSave(BillItem(
                                name            = itemName.trim(),
                                unitPrice       = price.toLongOrNull() ?: 0L,
                                quantity        = quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1,
                                assignedPersons = selectedPersons.toList()
                            ))
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(strings.addItem)
                }
            }
        }
    }
}