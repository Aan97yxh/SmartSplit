package com.smartsplit.app.presentation.bill

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.ui.components.*
import com.smartsplit.app.ui.theme.*
import com.smartsplit.app.util.LocalStrings
import com.smartsplit.app.util.ShareUtils
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.smartsplit.app.presentation.bill.BillViewModel
import com.smartsplit.app.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillSummaryScreen(
    billId: String,
    viewModel: BillViewModel,
    userEmail: String,
    onBack: () -> Unit,
    onDeleted: () -> Unit
) {
    val strings         = LocalStrings.current
    val bills           by viewModel.getBills(userEmail).collectAsState(initial = null)
    val bill            = bills?.find { it.id == billId }
    val context         = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReceiptPreview by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (bills == null) {
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    if (bill == null) {
        LaunchedEffect(Unit) { onDeleted() }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(strings.billSummary, fontWeight = FontWeight.Bold)
                        Text(bill.restaurantName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    // Share button
                    IconButton(onClick = { showReceiptPreview = true }) {
                        Icon(Icons.Default.Share, contentDescription = strings.shareBill,
                            tint = Color.White)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = strings.deleteBill,
                            tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = Primary,
                    titleContentColor          = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor     = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { GrandTotalCard(bill = bill, strings = strings) }

            item {
                Text(strings.yourShare,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
            }

            items(bill.persons) { person ->
                PersonShareCard(
                    person         = person,
                    bill           = bill,
                    strings        = strings,
                    onToggleSettle = { viewModel.togglePersonSettle(billId, person) }
                )
            }
        }
    }

    // ── Receipt share preview dialog ─────────────────────────────
    if (showReceiptPreview) {
        val graphicsLayer = rememberGraphicsLayer()
        Dialog(
            onDismissRequest = { showReceiptPreview = false },
            properties       = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(strings.shareBill,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(bottom = 12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier.drawWithContent {
                            graphicsLayer.record { this@drawWithContent.drawContent() }
                            drawLayer(graphicsLayer)
                        }
                    ) {
                        BillReceiptCard(bill = bill, strings = strings)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick  = { showReceiptPreview = false },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape    = RoundedCornerShape(12.dp)
                    ) { Text(strings.cancel) }

                    Button(
                        onClick = {
                            // Membungkus suspend function .toImageBitmap() dengan Coroutine Scope
                            scope.launch {
                                try {
                                    val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                        .copy(Bitmap.Config.ARGB_8888, false)
                                    ShareUtils.saveBitmapAndShare(context, bitmap)
                                    showReceiptPreview = false
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(strings.shareBill)
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title       = strings.deleteBill,
            message     = strings.deleteBillConfirm,
            confirmText = strings.delete,
            dismissText = strings.cancel,
            onConfirm   = { viewModel.deleteBill(bill); showDeleteDialog = false; onDeleted() },
            onDismiss   = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun GrandTotalCard(bill: Bill, strings: AppStrings) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier.size(40.dp).clip(CircleShape).background(PrimaryTint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Receipt, null, tint = Primary, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(strings.grandTotal, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiah(bill.grandTotal),
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(Modifier.height(10.dp))
            BillRow(strings.beforeTax, formatRupiah(bill.billSubtotal))
            Spacer(Modifier.height(6.dp))
            BillRow("${strings.tax} (${bill.taxPercent.toInt()}%)", formatRupiah(bill.billTax))
            Spacer(Modifier.height(6.dp))
            BillRow("${strings.service} (${bill.servicePercent.toInt()}%)", formatRupiah(bill.billService))
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                StatusBadge(
                    isSettled    = bill.isFullySettled,
                    settledLabel = strings.settled,
                    pendingLabel = "${bill.settledCount}/${bill.persons.size} ${strings.paid}"
                )
            }
        }
    }
}

@Composable
private fun BillRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PersonShareCard(
    person: String,
    bill: Bill,
    strings: AppStrings,
    onToggleSettle: () -> Unit
) {
    val isSettled = person in bill.settledPersons
    val isDark = MaterialTheme.colorScheme.surface == SurfaceDark

    val targetCardColor = when {
        isSettled && isDark -> Color(0xFF0C2421)
        isSettled && !isDark -> SettledBg
        else -> MaterialTheme.colorScheme.surface
    }

    val targetContentColor = if (isSettled && !isDark) {
        TextPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val cardColor by animateColorAsState(
        targetValue   = targetCardColor,
        animationSpec = tween(300), label = "cardColor"
    )

    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor   = targetContentColor
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarCircle(initial = person.firstOrNull() ?: '?', size = 36, textSize = 14)
                    Spacer(Modifier.width(10.dp))
                    Text(person, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(strings.afterTax, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiah(bill.totalForPerson(person)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = Primary)
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(Modifier.height(10.dp))

            Text(strings.orderedItems, style = MaterialTheme.typography.labelMedium,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp))

            bill.items.filter { person in it.assignedPersons }.forEach { item ->
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val personQuantity = item.quantity.toDouble() / item.assignedPersons.size
                    val qtyDisplay = if (personQuantity % 1.0 == 0.0) personQuantity.toInt().toString() else "%.1f".format(personQuantity)
                    val name = if (personQuantity != 1.0) "${item.name} ×$qtyDisplay" else item.name

                    Text(
                        text  = name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text  = formatRupiah(item.priceForPerson(person)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            PersonBreakdownRow(strings.subtotal, formatRupiah(bill.subtotalForPerson(person)))
            PersonBreakdownRow(strings.tax,      formatRupiah(bill.taxForPerson(person)))
            PersonBreakdownRow(strings.service,  formatRupiah(bill.serviceForPerson(person)))
            Spacer(Modifier.height(12.dp))

            Button(
                onClick  = onToggleSettle,
                modifier = Modifier.fillMaxWidth().height(42.dp),
                shape    = RoundedCornerShape(10.dp),
                colors   = if (isSettled)
                    ButtonDefaults.buttonColors(containerColor = Settled)
                else
                    ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(if (isSettled) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (isSettled) strings.markAsPending else strings.markAsPaid,
                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PersonBreakdownRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}