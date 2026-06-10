package com.smartsplit.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userEmail: String,
    val restaurantName: String,
    val date: String,
    val taxPercent: Float,
    val servicePercent: Float,
    val persons: List<String>,
    val items: List<BillItem>,
    val settledPersons: Set<String> = emptySet(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun subtotalForPerson(personName: String): Long =
        items.sumOf { it.priceForPerson(personName) }

    fun taxForPerson(personName: String): Long =
        (subtotalForPerson(personName) * taxPercent / 100).toLong()

    fun serviceForPerson(personName: String): Long =
        (subtotalForPerson(personName) * servicePercent / 100).toLong()

    fun totalForPerson(personName: String): Long =
        subtotalForPerson(personName) + taxForPerson(personName) + serviceForPerson(personName)

    val billSubtotal: Long get() = items.sumOf { it.totalPrice }
    val billTax: Long get() = (billSubtotal * taxPercent / 100).toLong()
    val billService: Long get() = (billSubtotal * servicePercent / 100).toLong()
    val grandTotal: Long get() = billSubtotal + billTax + billService
    val isFullySettled: Boolean get() = persons.isNotEmpty() && settledPersons.containsAll(persons)
    val settledCount: Int get() = persons.count { it in settledPersons }
}