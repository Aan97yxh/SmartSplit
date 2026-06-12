package com.smartsplit.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.ui.components.*
import com.smartsplit.app.ui.theme.*
import com.smartsplit.app.util.LocalStrings
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.runtime.saveable.rememberSaveable
import com.smartsplit.app.util.AppStrings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userEmail: String,
    onNewBill: () -> Unit,
    onBillTap: (String) -> Unit,
    onProfileTap: () -> Unit
) {
    val strings      = LocalStrings.current
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> }

    LaunchedEffect(userEmail) {
        viewModel.loadBills(userEmail)

        // Khusus Android 13 (API 33) upper
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isNotificationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            // Jika belum aktif, baru munculkan pop-up izin otomatis
            if (!isNotificationGranted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val allBills     by viewModel.bills.collectAsState()
    var searchQuery  by rememberSaveable { mutableStateOf("") }

    val filteredBills = remember(allBills, searchQuery) {
        if (searchQuery.isBlank()) allBills
        else allBills.filter { it.restaurantName.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        bottomBar = {
            BottomHomeBar(
                strings       = strings,
                onHomeTap     = {},
                onSettingsTap = onProfileTap,
                onNewBill     = onNewBill
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HomeHeader(
                    searchQuery = searchQuery,
                    onSearch    = { searchQuery = it },
                    onAvatarTap = onProfileTap,
                    strings     = strings
                )
            }
            item {
                Text(
                    strings.recentBills,
                    style    = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            if (filteredBills.isEmpty()) {
                item { EmptyBillsState(strings = strings) }
            } else {
                items(items = filteredBills, key = { it.id }) { bill ->
                    BillCard(bill = bill, strings = strings, onClick = { onBillTap(bill.id) })
                }
            }
        }
    }
}

@Composable
private fun BottomHomeBar(
    strings: AppStrings,
    onHomeTap: () -> Unit,
    onSettingsTap: () -> Unit,
    onNewBill: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier            = Modifier.weight(1f).clickable(onClick = onHomeTap),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Home, null, tint = Primary, modifier = Modifier.size(24.dp))
                Spacer(Modifier.height(2.dp))
                Text(strings.home, fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Medium)
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Primary)
                    .clickable(onClick = onNewBill),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Bill",
                    tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Column(
                modifier            = Modifier.weight(1f).clickable(onClick = onSettingsTap),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Settings, null,
                    tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp))
                Spacer(Modifier.height(2.dp))
                Text(strings.settings, fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun HomeHeader(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onAvatarTap: () -> Unit,
    strings: AppStrings
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Primary, PrimaryDark)))
            .padding(horizontal = 16.dp)
            .padding(top = 52.dp, bottom = 20.dp)
    ) {
        Column {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("SmartSplit",
                    color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable(onClick = onAvatarTap),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null,
                        tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = onSearch,
                placeholder   = { Text(strings.searchBills) },
                leadingIcon   = { Icon(Icons.Default.Search, null) },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(16.dp),
                colors        = OutlinedTextFieldDefaults.colors(

                    focusedContainerColor   = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor      = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // Border tipis elegan saat diklik
                    unfocusedBorderColor    = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun BillCard(
    bill: Bill,
    strings: AppStrings,
    onClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(bill.restaurantName,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.weight(1f))
                StatusBadge(
                    isSettled    = bill.isFullySettled,
                    settledLabel = strings.settled,
                    pendingLabel = strings.pending
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null,
                    tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(bill.date, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, null,
                            tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${bill.persons.size} ${strings.people}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (bill.persons.isNotEmpty()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            String.format(strings.paidOf, bill.settledCount, bill.persons.size),
                            style      = MaterialTheme.typography.bodySmall,
                            color      = if (bill.isFullySettled) Settled else Pending,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(strings.total, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiah(bill.grandTotal),
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmptyBillsState(strings: AppStrings) {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier.size(80.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Receipt, null,
                tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(strings.noBillsYet,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(strings.noBillsDesc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}