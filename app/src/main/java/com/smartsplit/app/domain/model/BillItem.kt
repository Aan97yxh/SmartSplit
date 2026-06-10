package com.smartsplit.app.domain.model

import java.util.UUID

data class BillItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val unitPrice: Long,
    val quantity: Int = 1,
    val assignedPersons: List<String>
) {
    val totalPrice: Long get() = unitPrice * quantity

    fun priceForPerson(personName: String): Long {
        if (!assignedPersons.contains(personName)) return 0L
        return if (assignedPersons.size == quantity) unitPrice
        else totalPrice / assignedPersons.size
    }
}