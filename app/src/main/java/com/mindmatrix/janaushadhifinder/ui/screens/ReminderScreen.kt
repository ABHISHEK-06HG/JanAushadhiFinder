package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.data.local.entity.IntakeRecordEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderTimeEntity
import com.mindmatrix.janaushadhifinder.data.model.IntakeStatus
import com.mindmatrix.janaushadhifinder.data.model.RepeatType
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.theme.*
import com.mindmatrix.janaushadhifinder.ui.viewmodel.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(navController: NavController, reminderViewModel: ReminderViewModel) {
    val reminders by reminderViewModel.allReminders.collectAsStateWithLifecycle()
    val history by reminderViewModel.allIntakeHistory.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(PrimaryBlue)) {
                TopAppBar(
                    title = { Text("Health Reminders", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue),
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Stats.route) }) {
                            Icon(Icons.Outlined.History, contentDescription = "History", tint = Color.White)
                        }
                    }
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.White
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Daily Doses", color = Color.White) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Monthly Refills", color = Color.White) }
                    )
                }
            }
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Screen.Reminders.route) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = PrimaryBlue) {
                Icon(Icons.Outlined.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (selectedTab == 0) {
                DailyRemindersList(reminders, reminderViewModel)
            } else {
                RefillRemindersList(reminders.filter { it.repeatType == RepeatType.CUSTOM })
            }
        }
    }

    if (showAddDialog) {
        AddReminderDialog(
            isRefill = selectedTab == 1,
            onDismiss = { showAddDialog = false },
            onSave = { reminder, times ->
                reminderViewModel.addReminder(reminder, times)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DailyRemindersList(reminders: List<ReminderEntity>, viewModel: ReminderViewModel) {
    val dailyReminders = reminders.filter { it.repeatType != RepeatType.CUSTOM }
    if (dailyReminders.isEmpty()) {
        EmptyRemindersView("No daily dose reminders")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dailyReminders, key = { it.id }) { reminder ->
                ReminderCard(reminder, viewModel)
            }
        }
    }
}

@Composable
fun RefillRemindersList(refillReminders: List<ReminderEntity>) {
    if (refillReminders.isEmpty()) {
        EmptyRemindersView("No monthly refill reminders")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(refillReminders, key = { it.id }) { reminder ->
                RefillCard(reminder)
            }
        }
    }
}

@Composable
fun ReminderCard(reminder: ReminderEntity, viewModel: ReminderViewModel) {
    val times by viewModel.getTimesForReminder(reminder.id).collectAsStateWithLifecycle(emptyList())

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightBlue,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Medication, contentDescription = null, tint = PrimaryBlue)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(reminder.medicineName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(reminder.dosage, fontSize = 13.sp, color = TextSecondary)
                }
                IconButton(onClick = { viewModel.deleteReminder(reminder) }) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "Delete", tint = AlertRed)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scheduled at: ", fontSize = 12.sp, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    times.forEach { time ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BackgroundGray
                        ) {
                            Text(
                                text = String.format("%02d:%02d", time.hour, time.minute),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            if (reminder.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = PrimaryBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(reminder.notes, fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun RefillCard(reminder: ReminderEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreen,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.EventRepeat, contentDescription = null, tint = AccentGreen)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(reminder.medicineName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Refill every 30 days", fontSize = 12.sp, color = AccentGreen, fontWeight = FontWeight.Medium)
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = null)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = 0.7f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = PrimaryBlue,
                trackColor = DividerColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("9 days left until next refill", fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun EmptyRemindersView(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.NotificationsActive, contentDescription = null, modifier = Modifier.size(64.dp), tint = DividerColor)
            Spacer(Modifier.height(16.dp))
            Text(message, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Tap + to create a new reminder", fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(isRefill: Boolean, onDismiss: () -> Unit, onSave: (ReminderEntity, List<ReminderTimeEntity>) -> Unit) {
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var repeatType by remember { mutableStateOf(if (isRefill) RepeatType.CUSTOM else RepeatType.DAILY) }
    val times = remember { mutableStateListOf<ReminderTimeEntity>() }
    
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isRefill) "Add Refill Reminder" else "Add Daily Dose", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = medicineName,
                    onValueChange = { medicineName = it },
                    label = { Text("Medicine Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                if (!isRefill) {
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosage (e.g. 1 Tablet)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Text("Frequency", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(RepeatType.DAILY, RepeatType.ALTERNATE_DAYS, RepeatType.WEEKLY).forEach { type ->
                            FilterChip(
                                selected = repeatType == type,
                                onClick = { repeatType = type },
                                label = { Text(type.name.lowercase().capitalize(), fontSize = 11.sp) }
                            )
                        }
                    }

                    Text("Reminder Times", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        times.forEachIndexed { index, time ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(BackgroundGray, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryBlue)
                                Spacer(Modifier.width(8.dp))
                                Text(String.format("%02d:%02d", time.hour, time.minute), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                                IconButton(onClick = { times.removeAt(index) }) {
                                    Icon(Icons.Outlined.Cancel, contentDescription = null, modifier = Modifier.size(18.dp), tint = AlertRed)
                                }
                            }
                        }
                        Button(
                            onClick = { showTimePicker = true },
                            colors = ButtonDefaults.buttonColors(containerColor = LightBlue, contentColor = PrimaryBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Outlined.AddAlarm, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Add Time Slot")
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = "30 Days",
                        onValueChange = {},
                        label = { Text("Refill Interval") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (e.g. After food)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reminder = ReminderEntity(
                        medicineName = medicineName,
                        dosage = if (isRefill) "30 Day Supply" else dosage,
                        notes = notes,
                        repeatType = repeatType
                    )
                    // For refill, add one default time if empty
                    if (isRefill && times.isEmpty()) {
                        times.add(ReminderTimeEntity(reminderId = 0, hour = 9, minute = 0))
                    }
                    onSave(reminder, times.toList())
                },
                enabled = medicineName.isNotEmpty() && (isRefill || times.isNotEmpty()),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) { Text("Save Reminder") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    times.add(ReminderTimeEntity(reminderId = 0, hour = timePickerState.hour, minute = timePickerState.minute))
                    showTimePicker = false
                }) { Text("Confirm") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        text = { content() }
    )
}
