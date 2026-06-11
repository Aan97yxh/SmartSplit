package com.smartsplit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.smartsplit.app.data.local.AppDatabase
import com.smartsplit.app.data.preferences.AppPreferences
import com.smartsplit.app.data.remote.RetrofitClient
import com.smartsplit.app.data.repository.BillRepositoryImpl
import com.smartsplit.app.data.repository.CurrencyRepositoryImpl
import com.smartsplit.app.data.repository.UserRepositoryImpl
import com.smartsplit.app.domain.model.Language
import com.smartsplit.app.navigation.SmartSplitNavGraph
import com.smartsplit.app.notification.BillReminderWorker
import com.smartsplit.app.notification.NotificationHelper
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.presentation.auth.AuthViewModelFactory
import com.smartsplit.app.presentation.bill.BillViewModelFactory
import com.smartsplit.app.presentation.home.HomeViewModelFactory
import com.smartsplit.app.presentation.settings.SettingsViewModel
import com.smartsplit.app.presentation.settings.SettingsViewModelFactory
import com.smartsplit.app.ui.theme.SmartSplitTheme
import com.smartsplit.app.util.EnglishStrings
import com.smartsplit.app.util.IndonesianStrings
import com.smartsplit.app.util.LocalStrings
import timber.log.Timber
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Timber init
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        // Notification channel
        NotificationHelper.createChannel(this)

        // Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        // Trigger notif saat pertama kali dibuka (demo)
        WorkManager.getInstance(this).cancelUniqueWork("SmartSplitBillReminder")

        // Notification Bill
        val reminderRequest = PeriodicWorkRequestBuilder<BillReminderWorker>(
            15, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SmartSplitBillReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )

        val prefs = AppPreferences(this)
        val db    = AppDatabase.getInstance(this)

        val userRepository     = UserRepositoryImpl(prefs, db.userDao())
        val billRepository     = BillRepositoryImpl(db.billDao())
        val currencyRepository = CurrencyRepositoryImpl(RetrofitClient.instance)

        val authFactory     = AuthViewModelFactory(userRepository)
        val billFactory     = BillViewModelFactory(billRepository, currencyRepository)
        val homeFactory     = HomeViewModelFactory(billRepository)
        val settingsFactory = SettingsViewModelFactory(userRepository)

        enableEdgeToEdge()

        setContent {
            val authVm: AuthViewModel         = viewModel(factory = authFactory)
            val settingsVm: SettingsViewModel = viewModel(factory = settingsFactory)

            val isDarkMode by settingsVm.isDarkMode.collectAsState()
            val language   by settingsVm.language.collectAsState()

            val strings = if (language == Language.INDONESIAN) IndonesianStrings else EnglishStrings

            SmartSplitTheme(darkTheme = isDarkMode) {
                CompositionLocalProvider(LocalStrings provides strings) {
                    androidx.compose.material3.Surface(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.background
                    ) {
                        SmartSplitNavGraph(
                            authViewModel = authVm,
                            billFactory = billFactory,
                            homeFactory = homeFactory,
                            settingsViewModel = settingsVm
                        )
                    }
                }
            }
        }
    }
}