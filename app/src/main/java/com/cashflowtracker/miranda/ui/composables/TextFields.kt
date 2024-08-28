package com.cashflowtracker.miranda.ui.composables

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.cashflowtracker.miranda.utils.LocationService
import com.cashflowtracker.miranda.utils.PermissionHandler
import com.cashflowtracker.miranda.utils.StartMonitoringResult

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
    modifier: Modifier
) {
    var isGps by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = locationService.coordinates) {
        if (locationService.coordinates != null) {
            location.value =
                "${locationService.coordinates?.latitude}, ${locationService.coordinates?.longitude}"
            isLocationLoaded.value = true
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

    OutlinedTextField(
        value = location.value,
        onValueChange = { text -> location.value = text },
        label = { Text("Location") },
        singleLine = true,
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