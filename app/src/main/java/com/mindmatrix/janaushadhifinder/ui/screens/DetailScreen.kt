package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import andr+oidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.theme.*
import com.mindmatrix.janaushadhifinder.ui.viewmodel.MedicineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, medicineViewModel: MedicineViewModel) {
    val medicineId = navController.currentBackStackEntry?.arguments?.getInt("medicineId") ?: -1
    var medicine by remember { mutableStateOf<MedicineEntity?>(null) }
    
    LaunchedEffect(medicineId) {
        medicine = medicineViewModel.getMedicineById(medicineId)
    }

    if (medicine == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryBlue)
        }
        return
    }

    val med = medicine!!
    val savings = med.priceBrand - med.priceGeneric
    val savingsPct = if (med.priceBrand > 0) ((savings / med.priceBrand) * 100).toInt() else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Comparison", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Medicine Info Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(med.brandName, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryBlue)
                            Text(med.category, fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = LightBlue) {
                            Text("Brand Name", fontSize = 10.sp, color = PrimaryBlue, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = DividerColor.copy(alpha = 0.5f))
                    Spacer(Modifier.height(12.dp))
                    
                    Text("Generic Equivalent", fontSize = 12.sp, color = TextSecondary)
                    Text(med.genericName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    
                    if (med.composition.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.Science, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Text(med.composition, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                        }
                    }
                }
            }

            // Price Comparison Section
            Text("Price Comparison", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // Branded
                    PriceBar(
                        label = "Branded Price",
                        price = med.priceBrand,
                        color = AlertRed,
                        isStrikethrough = true,
                        fillRatio = 1f
                    )

                    // Generic
                    PriceBar(
                        label = "Janaushadhi Price",
                        price = med.priceGeneric,
                        color = AccentGreen,
                        isStrikethrough = false,
                        fillRatio = (med.priceGeneric / med.priceBrand).toFloat().coerceIn(0.1f, 1f)
                    )
                }
            }

            // Savings Highlight
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreen.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("TOTAL SAVINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
                        Text("₹${String.format("%.2f", savings)}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = DarkBlue)
                        Text("Per unit/strip", fontSize = 10.sp, color = TextSecondary)
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AccentGreen
                    ) {
                        Text(
                            "$savingsPct% OFF",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Educational Info
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlue.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    Text(
                        "Jan Aushadhi medicines are quality-tested and certified by the Government of India. They are as effective as expensive branded medicines.",
                        fontSize = 12.sp, color = DarkBlue, lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(route = Screen.StoreList.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Outlined.Storefront, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Text("Find Nearest Store", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PriceBar(label: String, price: Double, color: Color, isStrikethrough: Boolean, fillRatio: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text(
                "₹${String.format("%.2f", price)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                style = if (isStrikethrough) androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else androidx.compose.ui.text.TextStyle()
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(DividerColor.copy(alpha = 0.3f))) {
            Box(modifier = Modifier.fillMaxWidth(fillRatio).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(color))
        }
    }
}
