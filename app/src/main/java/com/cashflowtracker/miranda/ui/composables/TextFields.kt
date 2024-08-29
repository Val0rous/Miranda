package com.cashflowtracker.miranda.ui.composables

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.PermissionHandler
import com.cashflowtracker.miranda.utils.StartMonitoringResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun PasswordTextField(
    password: MutableState<String>,
    modifier: Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password.value,
        onValueChange = { text -> password.value = text },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(
                onClick = {
                    passwordVisible = !passwordVisible
                },
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible) {
                        ImageVector.vectorResource(R.drawable.ic_visibility_off)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_visibility_filled)
                    }, contentDescription = "Toggle Visibility"
                )
            }
        },
        modifier = modifier
            .semantics {
                // Semantics for password autofill
                password()
                text = AnnotatedString(password.value)
            }
    )
}

@Composable
fun LocationTextField(
    location: MutableState<String>,
    locationService: LocationService,
    locationPermission: PermissionHandler,
    showLocationDisabledAlert: MutableState<Boolean>,
    isLocationLoaded: MutableState<Boolean>,
    coordinates: MutableState<Coordinates?>,
    isError: MutableState<Boolean>,
    modifier: Modifier
) {
    var isGps by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(key1 = locationService.coordinates) {
        if (locationService.coordinates != null) {
            coordinates.value = locationService.coordinates?.let {
                Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
            location.value =
                "${coordinates.value?.latitude}, ${coordinates.value?.longitude}"
            isLocationLoaded.value = true
            isGps = true
        }
    }

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            val res = locationService.requestCurrentLocation()
            showLocationDisabledAlert.value = res == StartMonitoringResult.GPSDisabled
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

//    fun CoroutineScope.performGeocoding(
//        placesClient: PlacesClient,
//        query: String,
//        onGeocodingSuccess: (LatLng) -> Unit,
//        onGeocodingFailure: (Exception) -> Unit
//    ) {
//        launch(Dispatchers.IO) {
//            try {
//                val placeFields = listOf(Place.Field.LAT_LNG)
//                val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//                val response = placesClient.findCurrentPlace(request)
//                response.addOnSuccessListener { findCurrentPlaceResponse ->
//                    val likelyPlace = findCurrentPlaceResponse.placeLikelihoods.firstOrNull()?.place
//                    val latLng = likelyPlace?.latLng
//                    if (latLng != null) {
//                        onGeocodingSuccess(latLng)
//                    } else {
//                        onGeocodingFailure(Exception("Geocoding failed: LatLng is null"))
//                    }
//                }.addOnFailureListener { exception -> onGeocodingFailure(exception) }
//            } catch (e: Exception) {
//                onGeocodingFailure(e)
//            }
//        }
//    }

    LaunchedEffect(location.value) {
        if (!isError.value) {
            snapshotFlow { location.value }
                .debounce(2000L)
                .collect { text ->
                    text.split(", ", limit = 2).let {
                        if (it.size == 2) {
                            coordinates.value = Coordinates(it[0].toDouble(), it[1].toDouble())
                            isLocationLoaded.value = true
                        }
                    }
                }
        }
    }

    OutlinedTextField(
        value = location.value,
        onValueChange = { text ->
            //location.value = text
            isGps = false
            location.value = text
            val isValidFormat =
                Regex("^-?\\d+(\\.\\d+)?, \\s*-?\\d+(\\.\\d+)?$").matches(text)
            if (text.isEmpty()) {
                isError.value = false
                isLocationLoaded.value = false
            } else if (!isValidFormat) {
                isError.value = true
                isLocationLoaded.value = false
            } else {
                isError.value = false
            }

//            if (text.isNotEmpty()) {
//                coroutineScope.performGeocoding(
//                    placesClient = Places.createClient(context),
//                    query = text,
//                    onGeocodingSuccess = { latLng ->
//                        coordinates.value = Coordinates(latLng.latitude, latLng.longitude)
//                    },
//                    onGeocodingFailure = { }
//                )
//            }
        },
        label = { Text("Location") },
        singleLine = true,
        isError = isError.value,
        placeholder = { Text("12.3456789, -98.7654321") },
        trailingIcon = {
            IconButton(
                onClick = {
                    requestLocation()
                    isGps = true
                    location.value = "Loading..."
                },
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Icon(
                    imageVector = if (isGps and isLocationLoaded.value) {
                        ImageVector.vectorResource(R.drawable.ic_my_location_filled)
                    } else if (isGps and !isLocationLoaded.value) {
                        ImageVector.vectorResource(R.drawable.ic_location_searching)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_my_location)
                    },
                    tint = if (isGps) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    contentDescription = "Get GPS location"
                )
            }
        },
        modifier = modifier
    )
}