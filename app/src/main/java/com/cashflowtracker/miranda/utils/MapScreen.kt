package com.cashflowtracker.miranda.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(latitude: Double, longitude: Double) {
    val coordinates = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordinates, 15f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState
    )
}