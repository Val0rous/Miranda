package com.cashflowtracker.miranda.ui.composables

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getProfilePicturePathFlow
import com.cashflowtracker.miranda.utils.rememberCameraLauncher
import com.cashflowtracker.miranda.utils.rememberPermission
import java.io.File

@Composable
fun ProfileImagePicker() {
    val context = LocalContext.current
    val cameraLauncher = rememberCameraLauncher()
    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val profilePicturePathFlow = remember { context.getProfilePicturePathFlow() }
    val profilePicturePath by profilePicturePathFlow.collectAsState(initial = null)

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() =
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }

    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray, CircleShape)
            .clickable {
                showDialog = true
            }
    ) {
        profilePicturePath?.let { path ->
            AsyncImage(
                model = ImageRequest.Builder(context).data(Uri.parse(path)).crossfade(true).build(),
                contentDescription = "Profile picture"
            )
        }
//        if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
//            AsyncImage(
//                ImageRequest.Builder(context)
//                    .data(cameraLauncher.capturedImageUri)
//                    .crossfade(true)
//                    .build(),
//                "Captured image"
//            )
//        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Choose Profile Picture") },
            text = { Text("Select an option") },
            confirmButton = {
                Button(onClick = {
                    //takePictureLauncher.launch(cameraUri)
                    takePicture()
                    showDialog = false
                }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                Button(onClick = {
                    pickImageLauncher.launch("image/*")
                    showDialog = false
                }) {
                    Text("Filesystem")
                }
            }
        )
    }
}