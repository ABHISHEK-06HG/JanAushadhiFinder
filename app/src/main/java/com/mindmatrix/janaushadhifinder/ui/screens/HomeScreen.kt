package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.theme.*
import com.mindmatrix.janaushadhifinder.ui.viewmodel.MedicineViewModel
import com.mindmatrix.janaushadhifinder.ui.viewmodel.LocationViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    medicineViewModel: MedicineViewModel,
    locationViewModel: LocationViewModel
) {
    val searchQuery by medicineViewModel.searchQuery.collectAsStateWithLifecycle()
    val medicines by medicineViewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by medicineViewModel.isLoading.collectAsStateWithLifecycle()
    val categories by medicineViewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by medicineViewModel.selectedCategory.collectAsStateWithLifecycle()
    val currentLocation by locationViewModel.currentLocation.collectAsStateWithLifecycle()
    val selectedState by locationViewModel.selectedState.collectAsStateWithLifecycle()
    val selectedCity by locationViewModel.selectedCity.collectAsStateWithLifecycle()
    
    var showLocationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(PrimaryBlue)) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.LocalPharmacy, contentDescription = null, tint = Color.White)
                            Text("Jan-Aushadhi Finder", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue),
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.StoreList.route) }) {
                            Icon(Icons.Outlined.LocationOn, contentDescription = "Stores", tint = Color.White)
                        }
                    }
                )
                
                // Location Selection Header
                Surface(
                    onClick = { showLocationDialog = true },
                    color = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.MyLocation, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Your Location", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (selectedCity.isNotEmpty()) "$selectedCity, $selectedState" else "Select Location",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Screen.Home.route) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = medicineViewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search brand (e.g. Dolo 650)", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = PrimaryBlue) },
                trailingIcon = {
                    Row {
                        IconButton(onClick = { navController.navigate(Screen.OCRScanner.route) }) {
                            Icon(Icons.Outlined.PhotoCamera, contentDescription = "Scan", tint = PrimaryBlue)
                        }
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { medicineViewModel.updateSearchQuery("") }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Clear")
                            }
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = DividerColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            Text("Categories", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { medicineViewModel.updateCategory(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                ShimmerLoadingList()
            } else if (medicines.isEmpty() && searchQuery.isNotEmpty()) {
                EmptyStateView()
            } else if (searchQuery.isEmpty()) {
                WelcomeView(medicineViewModel, navController)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(medicines, key = { it.id }) { medicine ->
                        MedicineCard(
                            medicine = medicine,
                            onClick = {
                                navController.navigate(Screen.Detail.createRoute(medicine.id))
                            }
                        )
                    }
                }
            }
        }
    }

    if (showLocationDialog) {
        LocationSelectionDialog(
            selectedState = selectedState,
            selectedCity = selectedCity,
            onStateSelected = { locationViewModel.selectState(it) },
            onCitySelected = { locationViewModel.selectCity(it) },
            onDismiss = { showLocationDialog = false }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open Request Dialog */ },
            colors = CardDefaults.cardColors(containerColor = BackgroundGray),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddShoppingCart, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Can't find a medicine?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Request your medicine here and we'll notify you when it's available.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun WelcomeView(viewModel: MedicineViewModel, navController: NavController) {
    val recent by viewModel.recentSearches.collectAsStateWithLifecycle()
    
    Column {
        Text("Recent Searches", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        recent.forEach { search ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.updateSearchQuery(search) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.History, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(search, color = TextSecondary, fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LightBlue),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Save up to 80%!", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 16.sp)
                    Text("Search for branded medicines and find their affordable Jan Aushadhi generic equivalents.", 
                        fontSize = 12.sp, color = DarkBlue)
                }
                Icon(Icons.Outlined.Savings, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open Request Dialog */ },
            colors = CardDefaults.cardColors(containerColor = BackgroundGray),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddShoppingCart, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Can't find a medicine?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Request your medicine here and we'll notify you when it's available.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun ShimmerLoadingList() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .shimmer()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open Request Dialog */ },
            colors = CardDefaults.cardColors(containerColor = BackgroundGray),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddShoppingCart, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Can't find a medicine?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Request your medicine here and we'll notify you when it's available.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.SearchOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
            Text("No results found", fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Try a different medicine name", fontSize = 13.sp, color = TextSecondary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open Request Dialog */ },
            colors = CardDefaults.cardColors(containerColor = BackgroundGray),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddShoppingCart, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Can't find a medicine?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Request your medicine here and we'll notify you when it's available.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun MedicineCard(medicine: MedicineEntity, onClick: () -> Unit) {
    val savings = medicine.priceBrand - medicine.priceGeneric
    val savingsPct = if (medicine.priceBrand > 0) ((savings / medicine.priceBrand) * 100).toInt() else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.brandName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = medicine.genericName,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = LightGreen
                ) {
                    Text(
                        text = "SAVE ₹${String.format("%.0f", savings)}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Branded Price", fontSize = 10.sp, color = TextSecondary)
                    Text("₹${medicine.priceBrand.toInt()}", fontSize = 14.sp, color = AlertRed,
                        style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
                }
                
                Icon(Icons.Outlined.TrendingDown, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(20.dp))
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Janaushadhi Price", fontSize = 10.sp, color = AccentGreen, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("₹${medicine.priceGeneric}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AccentGreen)
                        Spacer(Modifier.width(4.dp))
                        Text("($savingsPct% OFF)", fontSize = 11.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open Request Dialog */ },
            colors = CardDefaults.cardColors(containerColor = BackgroundGray),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddShoppingCart, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Can't find a medicine?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Request your medicine here and we'll notify you when it's available.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}
