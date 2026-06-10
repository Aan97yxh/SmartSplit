package com.smartsplit.app.domain.usecase.bill

import com.smartsplit.app.domain.repository.BillRepository

class TogglePersonSettleUseCase(private val repository: BillRepository) {
    suspend operator fun invoke(billId: String, personName: String) {
        val bill = repository.getBillById(billId) ?: return
        val newSettled = if (personName in bill.settledPersons) {
            bill.settledPersons - personName
        } else {
            bill.settledPersons + personName
        }
        repository.updateBill(bill.copy(settledPersons = newSettled))
    }
}