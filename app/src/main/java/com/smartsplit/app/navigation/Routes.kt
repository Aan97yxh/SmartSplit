package com.smartsplit.app.navigation

/** All named destinations in the app. */
object Routes {
    const val SPLASH       = "splash"
    const val ONBOARDING   = "onboarding"
    const val LOGIN        = "login"
    const val REGISTER     = "register"
    const val HOME         = "home"
    const val SETUP_BILL   = "setup_bill"
    const val ADD_ITEMS    = "add_items"
    const val BILL_SUMMARY = "bill_summary/{billId}"
    const val PROFILE      = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val SETTINGS     = "settings"

    fun billSummary(billId: String) = "bill_summary/$billId"
}