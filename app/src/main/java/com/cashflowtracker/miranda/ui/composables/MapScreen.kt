package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(latitude: Double, longitude: Double, isLocationLoaded: MutableState<Boolean>) {
    val coordinates = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordinates, 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .height(218.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLocationLoaded.value) {
            GoogleMap(
                modifier = Modifier.fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    mapToolbarEnabled = true,
                    myLocationButtonEnabled = true,
                    scrollGesturesEnabled = true,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = true,
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = true
                ),
            ) {
                Marker(
                    state = MarkerState(position = coordinates),
                    title = "", // Optional title
                    snippet = "Latitude: $latitude, Longitude: $longitude",  // Optional snippet
                )
            }
        } else {
            CircularProgressIndicator()
        }
    }
}