package com.mindmatrix.janaushadhifinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mindmatrix.janaushadhifinder.data.local.AppDatabase
import com.mindmatrix.janaushadhifinder.data.repository.LocationRepository
import com.mindmatrix.janaushadhifinder.data.repository.MedicineRepository
import com.mindmatrix.janaushadhifinder.data.repository.ReminderRepository
import com.mindmatrix.janaushadhifinder.data.repository.StoreRepository
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.screens.*
import com.mindmatrix.janaushadhifinder.ui.theme.JanAushadhiFinderTheme
import com.mindmatrix.janaushadhifinder.ui.viewmodel.LocationViewModel
import com.mindmatrix.janaushadhifinder.ui.viewmodel.MedicineViewModel
import com.mindmatrix.janaushadhifinder.ui.viewmodel.ReminderViewModel
import com.mindmatrix.janaushadhifinder.ui.viewmodel.StoreViewModel
import com.google.android.gms.location.LocationServices
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)
        val medicineRepo = MedicineRepository(db.medicineDao())
        val reminderRepo = ReminderRepository(db.reminderDao())
        val storeRepo = StoreRepository(db.storeDao())
        val locationRepo = LocationRepository(db.locationDao())

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            JanAushadhiFinderTheme {
                val locationViewModel: LocationViewModel = viewModel(
                    factory = LocationViewModel.Factory(locationRepo)
                )

                // Request Location Permission and detect current location
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { perms: Map<String, Boolean> ->
                    if (perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                        try {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                location?.let {
                                    locationViewModel.setLocationFromCoordinates(it.latitude, it.longitude, "Current Location")
                                }
                            }
                        } catch (e: SecurityException) {
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                    } else {
                        try {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                location?.let {
                                    locationViewModel.setLocationFromCoordinates(it.latitude, it.longitude, "Current Location")
                                }
                            }
                        } catch (e: SecurityException) {}
                    }
                }

                JanAushadhiMainApp(
                    medicineRepo = medicineRepo,
                    reminderRepo = reminderRepo,
                    storeRepo = storeRepo,
                    locationRepo = locationRepo,
                    locationViewModel = locationViewModel
                )
            }
        }
    }
}

@Composable
fun JanAushadhiMainApp(
    medicineRepo: MedicineRepository,
    reminderRepo: ReminderRepository,
    storeRepo: StoreRepository,
    locationRepo: LocationRepository,
    locationViewModel: LocationViewModel
) {
    val navController = rememberNavController()

    // ViewModels
    val medicineViewModel: MedicineViewModel = viewModel(
        factory = MedicineViewModel.Factory(medicineRepo)
    )
    val reminderViewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModel.Factory(androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application, reminderRepo)
    )
    val storeViewModel: StoreViewModel = viewModel(
        factory = StoreViewModel.Factory(storeRepo)
    )

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                medicineViewModel = medicineViewModel,
                locationViewModel = locationViewModel
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("medicineId") { type = NavType.IntType })
        ) {
            DetailScreen(navController = navController, medicineViewModel = medicineViewModel)
        }

        composable(Screen.StoreList.route) {
            StoreListScreen(
                navController = navController,
                storeViewModel = storeViewModel,
                locationViewModel = locationViewModel
            )
        }

        composable(Screen.Reminders.route) {
            ReminderScreen(navController = navController, reminderViewModel = reminderViewModel)
        }

        composable(Screen.Stats.route) {
            StatsScreen(navController = navController, reminderViewModel = reminderViewModel)
        }

        composable(Screen.OCRScanner.route) {
            OCRScannerScreen(navController = navController)
        }
    }
}
