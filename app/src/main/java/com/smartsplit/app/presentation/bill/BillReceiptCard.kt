package com.smartsplit.app.presentation.bill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.ui.components.formatRupiah
import com.smartsplit.app.ui.theme.Primary
import com.smartsplit.app.ui.theme.PrimaryTint
import com.smartsplit.app.ui.theme.Settled
import com.smartsplit.app.ui.theme.Pending
import com.smartsplit.app.util.AppStrings

@Composable
fun BillReceiptCard(bill: Bill, strings: AppStrings) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        // ── Header ───────────────────────────────────────────────
        Column(
            modifier            = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(Primary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("SS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text("SmartSplit", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
            Spacer(Modifier.height(4.dp))
            Text(bill.restaurantName,
                fontSize   = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign  = TextAlign.Center,
                color      = Color(0xFF1A1A2E)
            )
            Text(bill.date,
                fontSize = 13.sp,
                color    = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
        ReceiptDivider()
        Spacer(Modifier.height(12.dp))

        // ── Grand total ──────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(strings.grandTotal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(formatRupiah(bill.grandTotal),
                fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Primary)
        }
        Spacer(Modifier.height(4.dp))
        ReceiptRow(strings.beforeTax,   formatRupiah(bill.billSubtotal))
        ReceiptRow("${strings.receiptTax} (${bill.taxPercent.toInt()}%)", formatRupiah(bill.billTax))
        ReceiptRow("${strings.receiptService} (${bill.servicePercent.toInt()}%)", formatRupiah(bill.billService))

        Spacer(Modifier.height(12.dp))
        ReceiptDivider()

        // ── Per person ───────────────────────────────────────────
        bill.persons.forEach { person ->
            Spacer(Modifier.height(12.dp))

            val isSettled = person in bill.settledPersons

            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier         = Modifier
                            .size(28.dp)
                            .background(PrimaryTint, RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            person.first().uppercaseChar().toString(),
                            color      = Primary,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(person, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isSettled) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint     = if (isSettled) Settled else Pending,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        if (isSettled) strings.receiptStatusPaid else strings.receiptStatusPending,
                        fontSize   = 11.sp,
                        color      = if (isSettled) Settled else Pending,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Ordered items
            val personItems = bill.items.filter { person in it.assignedPersons }
            personItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 36.dp, top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val personQuantity = item.quantity.toDouble() / item.assignedPersons.size
                    val qtyDisplay = if (personQuantity % 1.0 == 0.0) personQuantity.toInt().toString() else "%.1f".format(personQuantity)
                    val name = if (personQuantity != 1.0) "${item.name} ×$qtyDisplay" else item.name

                    Text(name, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                    Text(formatRupiah(item.priceForPerson(person)),
                        fontSize = 12.sp, color = Color.Gray)
                }
            }

            // Breakdown
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(strings.receiptTotal,
                    fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
                Text(formatRupiah(bill.totalForPerson(person)),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Primary
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
        }

        Spacer(Modifier.height(16.dp))

        // ── Footer ───────────────────────────────────────────────
        Text(
            strings.receiptGeneratedBy,
            fontSize  = 11.sp,
            color     = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier  = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ReceiptDivider() {
    HorizontalDivider(
        color     = Color(0xFFDDDDDD),
        thickness = 1.dp
    )
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, color = Color.Gray)
    }
}