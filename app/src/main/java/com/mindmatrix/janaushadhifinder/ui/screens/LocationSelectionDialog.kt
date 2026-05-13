package com.mindmatrix.janaushadhifinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.janaushadhifinder.ui.theme.PrimaryBlue
import com.mindmatrix.janaushadhifinder.util.LocationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionDialog(
    selectedState: String,
    selectedCity: String,
    onStateSelected: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var stateSearch by remember { mutableStateOf("") }
    var citySearch by remember { mutableStateOf("") }
    var isSelectingState by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                if (isSelectingState) "Select State" else "Select City ($selectedState)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                OutlinedTextField(
                    value = if (isSelectingState) stateSearch else citySearch,
                    onValueChange = { if (isSelectingState) stateSearch = it else citySearch = it },
                    placeholder = { Text("Search...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    if (isSelectingState) {
                        val filteredStates = LocationUtils.indianStates.filter { it.contains(stateSearch, ignoreCase = true) }
                        items(filteredStates) { state ->
                            TextButton(
                                onClick = { 
                                    onStateSelected(state)
                                    isSelectingState = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(state, color = if (state == selectedState) PrimaryBlue else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    } else {
                        val cities = LocationUtils.stateCityMap[selectedState] ?: emptyList()
                        val filteredCities = cities.filter { it.contains(citySearch, ignoreCase = true) }
                        items(filteredCities) { city ->
                            TextButton(
                                onClick = { 
                                    onCitySelected(city)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(city, color = if (city == selectedCity) PrimaryBlue else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
                
                if (!isSelectingState) {
                    TextButton(onClick = { isSelectingState = true }) {
                        Text("← Back to States")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
