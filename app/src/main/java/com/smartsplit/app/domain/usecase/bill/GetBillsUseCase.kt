package com.smartsplit.app.domain.usecase.bill

import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetBillsUseCase(private val repository: BillRepository) {
    operator fun invoke(userEmail: String): Flow<List<Bill>> {
        return repository.getBillsByUser(userEmail)
    }
}