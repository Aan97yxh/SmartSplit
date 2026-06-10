package com.smartsplit.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.usecase.bill.GetBillsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(private val getBillsUseCase: GetBillsUseCase) : ViewModel() {

    private val _bills = MutableStateFlow<List<Bill>>(emptyList())
    val bills: StateFlow<List<Bill>> = _bills.asStateFlow()

    fun loadBills(userEmail: String) {
        viewModelScope.launch {
            getBillsUseCase(userEmail).collect { list ->
                _bills.value = list
                Timber.d("Bills updated: ${list.size} items")
            }
        }
    }
}