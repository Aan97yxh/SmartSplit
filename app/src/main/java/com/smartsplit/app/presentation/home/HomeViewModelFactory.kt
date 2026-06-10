package com.smartsplit.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartsplit.app.domain.repository.BillRepository
import com.smartsplit.app.domain.usecase.bill.GetBillsUseCase

class HomeViewModelFactory(private val repository: BillRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getBillsUseCase = GetBillsUseCase(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}