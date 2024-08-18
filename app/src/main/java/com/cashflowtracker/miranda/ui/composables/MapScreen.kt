package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun MapScreen(latitude: Double, longitude: Double) {
    val coordinates = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordinates, 15f)
    }
    var mapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) { // Launch a coroutine when the composable enters the composition
        // Simulate map loading delay (replace with actual map loading logic)
        delay(2000)
        mapLoaded = true // Set mapLoaded to true once the map is loaded
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .height(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (mapLoaded) {
            GoogleMap(
                modifier = Modifier.fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    myLocationButtonEnabled = true,
                    scrollGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = false
                ),
            )
        } else {
            CircularProgressIndicator()
        }
    }
}