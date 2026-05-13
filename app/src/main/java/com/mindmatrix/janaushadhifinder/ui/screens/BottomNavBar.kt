package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.theme.PrimaryBlue

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                if (currentRoute != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) { launchSingleTop = true }
                }
            },
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.StoreList.route,
            onClick = {
                if (currentRoute != Screen.StoreList.route) {
                    navController.navigate(Screen.StoreList.route) { launchSingleTop = true }
                }
            },
            icon = { Icon(Icons.Outlined.LocationOn, contentDescription = "Stores") },
            label = { Text("Stores") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Reminders.route,
            onClick = {
                if (currentRoute != Screen.Reminders.route) {
                    navController.navigate(Screen.Reminders.route) { launchSingleTop = true }
                }
            },
            icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Reminders") },
            label = { Text("Reminders") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
    }
}
