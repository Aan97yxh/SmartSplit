package com.smartsplit.app.domain.repository

import com.smartsplit.app.domain.model.Bill
import kotlinx.coroutines.flow.Flow

interface BillRepository {
    fun getBillsByUser(userEmail: String): Flow<List<Bill>>
    suspend fun getBillsByUserOnce(userEmail: String): List<Bill>
    suspend fun addBill(bill: Bill)
    suspend fun updateBill(bill: Bill)
    suspend fun deleteBill(bill: Bill)
    suspend fun getBillById(id: String): Bill?
}