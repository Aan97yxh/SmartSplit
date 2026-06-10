package com.smartsplit.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.smartsplit.app.presentation.auth.AuthViewModel
import com.smartsplit.app.presentation.bill.BillViewModel
import com.smartsplit.app.presentation.bill.BillViewModelFactory
import com.smartsplit.app.presentation.home.HomeViewModel
import com.smartsplit.app.presentation.home.HomeViewModelFactory
import com.smartsplit.app.presentation.settings.SettingsViewModel
import com.smartsplit.app.presentation.auth.LoginScreen
import com.smartsplit.app.presentation.auth.RegisterScreen
import com.smartsplit.app.presentation.bill.AddItemsScreen
import com.smartsplit.app.presentation.bill.BillSummaryScreen
import com.smartsplit.app.presentation.bill.SetupBillScreen
import com.smartsplit.app.presentation.home.HomeScreen
import com.smartsplit.app.presentation.onboarding.OnboardingScreen
import com.smartsplit.app.presentation.settings.EditProfileScreen
import com.smartsplit.app.presentation.settings.ProfileScreen
import com.smartsplit.app.presentation.settings.SettingsScreen
import com.smartsplit.app.presentation.splash.SplashScreen

@Composable
fun SmartSplitNavGraph(
    authViewModel    : AuthViewModel,
    billFactory      : BillViewModelFactory,
    homeFactory      : HomeViewModelFactory,
    settingsViewModel: SettingsViewModel
) {
    val navController  = rememberNavController()
    val isLoggedIn     by authViewModel.isLoggedIn.collectAsState()
    val hasSeenOnboarding by authViewModel.hasSeenOnboarding.collectAsState()
    val userState      by authViewModel.user.collectAsState()

    val billVm: BillViewModel = viewModel(factory = billFactory)
    val homeVm: HomeViewModel = viewModel(factory = homeFactory)

    val slideSpec = tween<androidx.compose.ui.unit.IntOffset>(300)
    val fadeSpec  = tween<Float>(300)

    NavHost(
        navController    = navController,
        startDestination = Routes.SPLASH,
        enterTransition  = { slideInHorizontally(slideSpec) { it } + fadeIn(fadeSpec) },
        exitTransition   = { slideOutHorizontally(tween(300)) { -it / 3 } + fadeOut(fadeSpec) },
        popEnterTransition  = { slideInHorizontally(slideSpec) { -it } + fadeIn(fadeSpec) },
        popExitTransition   = { slideOutHorizontally(tween(300)) { it } + fadeOut(fadeSpec) }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val dest = when {
                        !hasSeenOnboarding -> Routes.ONBOARDING
                        !isLoggedIn        -> Routes.LOGIN
                        else               -> Routes.HOME
                    }
                    navController.navigate(dest) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onDone = {
                    authViewModel.markOnboardingDone()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel            = authViewModel,
                onLoginSuccess       = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel         = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel    = homeVm,
                userEmail    = userState.email,
                onNewBill    = { navController.navigate(Routes.SETUP_BILL) },
                onBillTap    = { billId -> navController.navigate(Routes.billSummary(billId)) },
                onProfileTap = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.SETUP_BILL) {
            SetupBillScreen(
                viewModel  = billVm,
                onContinue = { navController.navigate(Routes.ADD_ITEMS) },
                onBack     = { navController.popBackStack() }
            )
        }

        composable(Routes.ADD_ITEMS) {
            AddItemsScreen(
                viewModel     = billVm,
                userEmail     = userState.email,
                onViewSummary = { billId ->
                    navController.navigate(Routes.billSummary(billId)) {
                        popUpTo(Routes.HOME)
                    }
                },
                onBack        = { navController.popBackStack() }
            )
        }

        composable(
            route     = Routes.BILL_SUMMARY,
            arguments = listOf(navArgument("billId") { type = NavType.StringType })
        ) { backStackEntry ->
            val billId = backStackEntry.arguments?.getString("billId") ?: return@composable
            BillSummaryScreen(
                billId    = billId,
                viewModel = billVm,
                userEmail = userState.email,
                onBack    = { navController.popBackStack() },
                onDeleted = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                settingsViewModel   = settingsViewModel,
                authViewModel       = authViewModel,
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onLogout            = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onBack              = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                authViewModel = authViewModel,
                onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                onBack        = { navController.popBackStack() }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                viewModel = authViewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}