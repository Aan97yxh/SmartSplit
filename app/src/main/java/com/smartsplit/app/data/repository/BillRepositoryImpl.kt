package com.smartsplit.app.data.repository

import com.smartsplit.app.data.local.dao.BillDao
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class BillRepositoryImpl(private val dao: BillDao) : BillRepository {
    override fun getBillsByUser(userEmail: String): Flow<List<Bill>> {
        return dao.getBillsByUser(userEmail)
    }

    override suspend fun addBill(bill: Bill) {
        dao.insertBill(bill)
    }

    override suspend fun updateBill(bill: Bill) {
        dao.updateBill(bill)
    }

    override suspend fun deleteBill(bill: Bill) {
        dao.deleteBill(bill)
    }

    override suspend fun getBillById(id: String): Bill? {
        return dao.getBillById(id)
    }
}