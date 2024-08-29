package com.cashflowtracker.miranda.ui.composables

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.createRoundedMarkerIcon
import com.cashflowtracker.miranda.utils.iconFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    latitude: Double,
    longitude: Double,
    isLocationLoaded: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val coordinates = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordinates, 15f)
    }

    Box(
        modifier = modifier
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

@Composable
fun FullScreenMapView(transactions: List<Transaction>) {
    val initialCoordinates = transactions.firstOrNull()?.location?.split(", ", limit = 2)?.let {
        if (it.size == 2) {
            LatLng(it[0].toDouble(), it[1].toDouble())
        } else {
            null
        }
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialCoordinates, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = true,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = true,
            scrollGesturesEnabled = true,
            tiltGesturesEnabled = true,
            rotationGesturesEnabled = true,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = true
        )
    ) {
        transactions.forEach { transaction ->
            val coordinates = transaction.location?.split(", ", limit = 2)?.let {
                if (it.size == 2) {
                    Coordinates(it[0].toDouble(), it[1].toDouble())
                } else {
                    null
                }
            }
            if (coordinates != null) {
                val markerIcon = createRoundedMarkerIcon(
                    type = transaction.type,
                    source = transaction.source,
                    destination = transaction.destination
                )
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            coordinates.latitude,
                            coordinates.longitude
                        )
                    ),
                    title = transaction.comment ?: transaction.location,
                    snippet = transaction.type,
                    icon = markerIcon
                )
            }
        }
    }
}