package com.smartsplit.app.data.local.dao

import androidx.room.*
import com.smartsplit.app.domain.model.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE userEmail = :userEmail ORDER BY createdAt DESC")
    fun getBillsByUser(userEmail: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE userEmail = :userEmail ORDER BY createdAt DESC")
    suspend fun getBillsByUserOnce(userEmail: String): List<Bill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)

    @Update
    suspend fun updateBill(bill: Bill)

    @Delete
    suspend fun deleteBill(bill: Bill)

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: String): Bill?
}