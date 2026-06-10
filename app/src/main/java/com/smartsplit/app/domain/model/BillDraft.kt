package com.smartsplit.app.domain.model

data class BillDraft(
    val restaurantName: String = "",
    val date: String = "",
    val taxPercent: Float = 10f,
    val servicePercent: Float = 5f,
    val persons: List<String> = emptyList(),
    val items: List<BillItem> = emptyList()
) {
    fun toBill(userEmail: String): Bill = Bill(
        userEmail      = userEmail,
        restaurantName = restaurantName,
        date           = date,
        taxPercent     = taxPercent,
        servicePercent = servicePercent,
        persons        = persons,
        items          = items
    )
}