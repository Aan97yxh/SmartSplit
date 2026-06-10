package com.smartsplit.app.domain.usecase.bill

import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.repository.BillRepository

class FinalizeBillUseCase(private val repository: BillRepository) {
    suspend operator fun invoke(bill: Bill) {
        repository.addBill(bill)
    }
}