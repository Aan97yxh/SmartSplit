package com.smartsplit.app.presentation.bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.model.BillDraft
import com.smartsplit.app.domain.model.BillItem
import com.smartsplit.app.domain.usecase.bill.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class BillViewModel(
    private val getBillsUseCase: GetBillsUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val finalizeBillUseCase: FinalizeBillUseCase,
    private val deleteBillUseCase: DeleteBillUseCase,
    private val togglePersonSettleUseCase: TogglePersonSettleUseCase
) : ViewModel() {

    fun getBills(userEmail: String): Flow<List<Bill>> {
        return getBillsUseCase(userEmail)
    }

    private val _exchangeRate = MutableStateFlow<Double?>(null)
    val exchangeRate: StateFlow<Double?> = _exchangeRate.asStateFlow()

    private val _isCurrencyLoading = MutableStateFlow(false)
    val isCurrencyLoading: StateFlow<Boolean> = _isCurrencyLoading.asStateFlow()

    // Ambil Data API
    fun fetchExchangeRate(fromCurrency: String) {
        viewModelScope.launch {
            _isCurrencyLoading.value = true
            val rate = getExchangeRateUseCase(fromCurrency)
            _exchangeRate.value = rate
            _isCurrencyLoading.value = false
        }
    }

    fun clearExchangeRate() {
        _exchangeRate.value = null
    }

    // ── Draft ─────────────────────────────────────────────────────
    private val _draft = MutableStateFlow<BillDraft?>(null)
    val draft: StateFlow<BillDraft?> = _draft.asStateFlow()

    fun startDraft(draft: BillDraft) {
        _draft.value = draft
        Timber.d("Draft started: ${draft.restaurantName}")
    }

    fun updateDraftItems(items: List<BillItem>) {
        _draft.update { it?.copy(items = items) }
    }

    // Simpan tagihan final
    fun finalizeDraft(userEmail: String): Bill? {
        val bill = _draft.value?.toBill(userEmail) ?: return null
        viewModelScope.launch {
            finalizeBillUseCase(bill)
            Timber.d("Bill finalized and saved: ${bill.id}")
        }
        _draft.value = null
        return bill
    }

    fun clearDraft() {
        _draft.value = null
    }

    // ── Bill actions via UseCase ──────────────────────────────────
    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            deleteBillUseCase(bill)
            Timber.d("Bill deleted: ${bill.id}")
        }
    }

    fun togglePersonSettle(billId: String, personName: String) {
        viewModelScope.launch {
            togglePersonSettleUseCase(billId, personName)
            Timber.d("Toggled settle for $personName in bill $billId")
        }
    }
}