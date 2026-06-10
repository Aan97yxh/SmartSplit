package com.smartsplit.app.presentation.bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartsplit.app.domain.repository.BillRepository
import com.smartsplit.app.domain.repository.CurrencyRepository
import com.smartsplit.app.domain.usecase.bill.*

class BillViewModelFactory(
    private val billRepository: BillRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return BillViewModel(
                getBillsUseCase = GetBillsUseCase(billRepository),
                getExchangeRateUseCase = GetExchangeRateUseCase(currencyRepository),
                finalizeBillUseCase = FinalizeBillUseCase(billRepository),
                deleteBillUseCase = DeleteBillUseCase(billRepository),
                togglePersonSettleUseCase = TogglePersonSettleUseCase(billRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}