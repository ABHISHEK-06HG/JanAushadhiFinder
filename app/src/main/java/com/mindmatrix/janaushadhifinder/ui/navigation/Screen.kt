package com.mindmatrix.janaushadhifinder.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Detail : Screen("detail/{medicineId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
    object StoreList : Screen("stores")
    object Reminders : Screen("reminders")
    object Stats : Screen("stats")
    object OCRScanner : Screen("ocr_scanner")
}
