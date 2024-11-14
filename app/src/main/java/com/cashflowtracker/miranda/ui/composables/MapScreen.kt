package com.cashflowtracker.miranda.ui.composables

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.screens.ViewTransaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.viewmodels.ThemeViewModel
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.createRoundedMarkerIcon
import com.cashflowtracker.miranda.utils.iconFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MapScreen(
    latitude: Double,
    longitude: Double,
    isLocationLoaded: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val followSystem by themeViewModel.followSystem.collectAsState()
    val effectiveIsDarkTheme = if (followSystem) {
        isSystemInDarkTheme()
    } else {
        isDarkTheme
    }

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
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapColorScheme(
                        if (effectiveIsDarkTheme) {
                            MapColorScheme.DARK
                        } else {
                            MapColorScheme.LIGHT
                        }
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenMapView(transactions: List<Transaction>) {
    var showDialog by remember { mutableStateOf(false) }
    var openTransaction by remember { mutableStateOf<Transaction?>(null) }
    val context = LocalContext.current
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val followSystem by themeViewModel.followSystem.collectAsState()
    val effectiveIsDarkTheme = if (followSystem) {
        isSystemInDarkTheme()
    } else {
        isDarkTheme
    }

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
        ),
        googleMapOptionsFactory = {
            GoogleMapOptions().mapColorScheme(
                if (effectiveIsDarkTheme) {
                    MapColorScheme.DARK
                } else {
                    MapColorScheme.LIGHT
                }
            )
        }
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
                    icon = if (openTransaction != transaction) {
                        markerIcon
                    } else {
                        null
                    },
                    onClick = {
                        showDialog = true
                        openTransaction = transaction
                        true // Return false to keep default behavior, true to prevent default behavior
                    }
                )
            }
        }
    }

    val view = LocalView.current
    val window = (view.context as Activity).window
    if (showDialog) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            onDismissRequest = {
                showDialog = false
                openTransaction = null
            }
        ) {
            ListItem(
                leadingContent = {
                    IconWithBackground(
                        icon = iconFactory(
                            openTransaction!!.type,
                            openTransaction!!.source,
                            openTransaction!!.destination
                        ),
                        iconSize = 24.dp,
                        iconColor = LocalCustomColors.current.icon,
                        backgroundSize = 40.dp,
                        backgroundColor = when (openTransaction!!.type) {
                            "Output" -> LocalCustomColors.current.surfaceTintRed
                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                            else -> LocalCustomColors.current.surfaceTintBlue
                        }
                    )
                },
                overlineContent = {
                    Text(
                        text = ZonedDateTime.parse(
                            openTransaction!!.createdOn,
                            DateTimeFormatter.ISO_ZONED_DATE_TIME
                        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd  ·  HH:mm")),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                headlineContent = {
                    Text(
                        text = openTransaction!!.comment ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                trailingContent = {
                    Text(
                        text = when (openTransaction!!.type) {
                            "Output" -> if (openTransaction!!.amount != 0.0) "-%.2f €" else "%.2f €"
                            "Input" -> if (openTransaction!!.amount != 0.0) "+%.2f €" else "%.2f €"
                            else -> "%.2f €"
                        }.format(openTransaction!!.amount),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
//                                when (transaction.type) {
//                                    "Output" -> CustomColors.current.surfaceTintRed
//                                    "Input" -> CustomColors.current.surfaceTintGreen
//                                    else -> CustomColors.current.surfaceTintBlue
//                                }
                    )
                },
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(7.dp)
                    )
                    .clickable {
                        val intent = Intent(context, ViewTransaction::class.java)
                        intent.putExtra("transactionId", openTransaction!!.transactionId.toString())
                        context.startActivity(intent)
                    }
            )

            val color = MaterialTheme.colorScheme.surfaceContainerLow
            SideEffect {
                window.navigationBarColor = color.toArgb()
            }
        }
    } else {
        val color = Color.Transparent
        SideEffect {
            window.navigationBarColor = color.toArgb()
        }
    }
}