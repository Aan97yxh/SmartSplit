package com.smartsplit.app.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smartsplit.app.data.local.AppDatabase
import com.smartsplit.app.data.preferences.AppPreferences
import com.smartsplit.app.data.repository.BillRepositoryImpl
import com.smartsplit.app.domain.repository.BillRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

class BillReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val db = AppDatabase.getInstance(applicationContext)
    private val repository: BillRepository = BillRepositoryImpl(db.billDao())
    private val prefs = AppPreferences(applicationContext)

    override suspend fun doWork(): Result {
        try {
            if (!prefs.notificationsEnabled) {
                Timber.d("WorkManager: Notifikasi dinonaktifkan oleh pengguna di pengaturan. Melewati pengiriman.")
                return Result.success()
            }

            val userEmail = prefs.userEmail
            if (userEmail.isBlank()) return Result.failure()
            val allBills = repository.getBillsByUserOnce(userEmail)
            val pendingBills = allBills.filter { !it.isFullySettled }

            if (pendingBills.isNotEmpty()) {
                NotificationHelper.showBillReminderNotification(
                    context = applicationContext,
                    pendingBillsCount = pendingBills.size
                )
                Timber.d("WorkManager: Sukses mengirim push pengingat untuk ${pendingBills.size} tagihan.")
            } else {
                Timber.d("WorkManager: Aman, semua tagihan sudah lunas.")
            }

            return Result.success()
        } catch (e: Exception) {
            Timber.e(e, "WorkManager: Gagal mengecek tagihan")
            return Result.retry()
        }
    }
}