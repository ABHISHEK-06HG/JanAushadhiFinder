package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.data.model.IntakeStatus
import com.mindmatrix.janaushadhifinder.ui.theme.*
import com.mindmatrix.janaushadhifinder.ui.viewmodel.ReminderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController, reminderViewModel: ReminderViewModel) {
    val history by reminderViewModel.allIntakeHistory.collectAsStateWithLifecycle()
    
    val takenCount = history.count { it.status == IntakeStatus.TAKEN }
    val missedCount = history.count { it.status == IntakeStatus.MISSED }
    val snoozedCount = history.count { it.status == IntakeStatus.SNOOZED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Dashboard", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Adherence Statistics", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("Taken", takenCount.toString(), AccentGreen, Modifier.weight(1f))
                    StatCard("Missed", missedCount.toString(), AlertRed, Modifier.weight(1f))
                    StatCard("Snoozed", snoozedCount.toString(), Color(0xFFFBC02D), Modifier.weight(1f))
                }
            }
            
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Weekly Performance", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Simple Bar Chart Placeholder
                        Row(
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(7) { index ->
                                val height = (20..100).random()
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .fillMaxHeight(height / 100f)
                                        .background(PrimaryBlue, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
