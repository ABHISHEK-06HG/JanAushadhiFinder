package com.mindmatrix.janaushadhifinder.ui.screens

import android.content.Intent
import android.net.Uri
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity
import com.mindmatrix.janaushadhifinder.ui.model.StoreUIModel
import com.mindmatrix.janaushadhifinder.ui.navigation.Screen
import com.mindmatrix.janaushadhifinder.ui.theme.*
import com.mindmatrix.janaushadhifinder.ui.viewmodel.StoreViewModel
import com.mindmatrix.janaushadhifinder.ui.viewmodel.LocationViewModel
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.infowindow.InfoWindow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    locationViewModel: LocationViewModel
) {
    val stores by storeViewModel.nearbyStores.collectAsStateWithLifecycle()
    val currentLocation by locationViewModel.currentLocation.collectAsStateWithLifecycle()
    var isMapView by remember { mutableStateOf(false) }
    var selectedStoreId by remember { mutableStateOf<Int?>(null) }
    
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Update VM with current location for distance calculation
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            storeViewModel.updateUserLocation(it.latitude, it.longitude)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jan Aushadhi Kendras", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { isMapView = !isMapView }) {
                        Icon(
                            if (isMapView) Icons.Outlined.FormatListBulleted else Icons.Outlined.Map,
                            contentDescription = "Toggle View",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Screen.StoreList.route) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isMapView) {
                OSMMapView(
                    stores = stores,
                    center = GeoPoint(currentLocation?.latitude ?: 12.3082, currentLocation?.longitude ?: 76.6450),
                    selectedStoreId = selectedStoreId,
                    onStoreSelected = { selectedStoreId = it }
                )
            } else {
                if (stores.isEmpty()) {
                    EmptyStoresView()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Found ${stores.size} stores near you",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        items(stores, key = { it.store.id }) { storeUI ->
                            StoreCard(
                                storeUI = storeUI,
                                isSelected = selectedStoreId == storeUI.store.id,
                                onClick = {
                                    selectedStoreId = storeUI.store.id
                                    isMapView = true // Switch to map to show selected store
                                }
                            )
                        }
                    }
                }
            }
            
            // Recenter Floating Button
            FloatingActionButton(
                onClick = { 
                    // Logic to recenter map if in map view
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color.White,
                contentColor = PrimaryBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.MyLocation, contentDescription = "My Location")
            }
        }
    }
}

@Composable
fun OSMMapView(
    stores: List<StoreUIModel>,
    center: GeoPoint,
    selectedStoreId: Int?,
    onStoreSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    
    // Initialize OSMDroid configuration
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val mapView = remember { MapView(context) }

    AndroidView(
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
                controller.setZoom(14.0)
                controller.setCenter(center)
                
                // User location marker
                val userMarker = Marker(this)
                userMarker.position = center
                userMarker.icon = context.getDrawable(android.R.drawable.presence_online)
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                userMarker.title = "Your Location"
                overlays.add(userMarker)

                stores.forEach { storeUI ->
                    val marker = Marker(this)
                    marker.position = GeoPoint(storeUI.store.latitude, storeUI.store.longitude)
                    marker.title = storeUI.store.name
                    marker.snippet = "${storeUI.store.address}\nDistance: ${storeUI.formattedDistance}"
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    
                    marker.setOnMarkerClickListener { m, _ ->
                        m.showInfoWindow()
                        onStoreSelected(storeUI.store.id)
                        true
                    }
                    
                    overlays.add(marker)
                    
                    if (selectedStoreId == storeUI.store.id) {
                        controller.animateTo(marker.position)
                        marker.showInfoWindow()
                        
                        // Add a simple line from user to store as a "route"
                        val line = Polyline(this)
                        line.addPoint(center)
                        line.addPoint(marker.position)
                        line.outlinePaint.color = android.graphics.Color.BLUE
                        line.outlinePaint.strokeWidth = 5f
                        overlays.add(line)
                    }
                }
            }
        },
        update = { view ->
            // Clear previous polylines
            view.overlays.removeAll { it is Polyline }
            
            selectedStoreId?.let { id ->
                stores.find { it.store.id == id }?.let { storeUI ->
                    val storePos = GeoPoint(storeUI.store.latitude, storeUI.store.longitude)
                    view.controller.animateTo(storePos)
                    
                    // Redraw route line on update
                    val line = Polyline(view)
                    line.addPoint(center)
                    line.addPoint(storePos)
                    line.outlinePaint.color = android.graphics.Color.BLUE
                    line.outlinePaint.strokeWidth = 5f
                    view.overlays.add(line)
                    view.invalidate()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun StoreCard(storeUI: StoreUIModel, isSelected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val store = storeUI.store

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LightBlue else SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Storefront, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        store.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.NearMe, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(storeUI.formattedDistance, fontSize = 12.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (store.isAvailable) LightGreen else Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = if (store.isAvailable) "OPEN" else "CLOSED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (store.isAvailable) AccentGreen else Color(0xFFE65100),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                store.address,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${store.phoneNumber}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Outlined.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:${store.latitude},${store.longitude}?q=${Uri.encode(store.name)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Outlined.Directions, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Directions", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EmptyStoresView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Icon(Icons.Outlined.LocationOff, contentDescription = null, modifier = Modifier.size(80.dp), tint = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No Stores Found",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimary
            )
            Text(
                "We couldn't find any Jan Aushadhi Kendras in this area. Try selecting a different city.",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}
