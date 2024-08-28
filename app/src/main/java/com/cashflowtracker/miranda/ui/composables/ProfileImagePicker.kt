package com.cashflowtracker.miranda.ui.composables

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.clearProfilePicturePath
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getProfilePicturePathFlow
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.saveProfilePicturePath
import com.cashflowtracker.miranda.utils.rememberCameraLauncher
import com.cashflowtracker.miranda.utils.rememberPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImagePicker() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val cameraLauncher = rememberCameraLauncher()
    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val profilePicturePathFlow = remember { context.getProfilePicturePathFlow() }
    val profilePicturePath by profilePicturePathFlow.collectAsState(initial = null)

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
//            }
            coroutineScope.launch(Dispatchers.IO) {
                context.saveProfilePicturePath(uri.toString())
            }
        }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val readStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermission(Manifest.permission.READ_MEDIA_IMAGES) { status ->
            if (status.isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        rememberPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { status ->
            if (status.isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun takePicture() =
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }

    fun choosePicture() =
        if (readStoragePermission.status.isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            readStoragePermission.launchPermissionRequest()
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
        ModalBottomSheet(
            onDismissRequest = { showDialog = false }
        ) {
            Text(
                text = "Choose Profile Picture",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    IconButton(
                        onClick = {
                            takePicture()
                            showDialog = false
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_photo_camera),
                            contentDescription = "Camera",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(text = "Camera", modifier = Modifier.padding(top = 6.dp))
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    IconButton(
                        onClick = {
                            choosePicture()
                            showDialog = false
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_photo_library),
                            contentDescription = "Gallery",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(text = "Gallery", modifier = Modifier.padding(top = 6.dp)) // Filesystem
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                context.clearProfilePicturePath()
                                showDialog = false
                            }
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .border(1.dp, MaterialTheme.colorScheme.error, CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete_forever),
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "Remove",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp)
                    ) // Filesystem
                }
            }
        }
    }

//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Choose Profile Picture") },
//            text = { Text("Select an option") },
//            confirmButton = {
//                Button(onClick = {
//                    //takePictureLauncher.launch(cameraUri)
//                    takePicture()
//                    showDialog = false
//                }) {
//                    Text("Camera")
//                }
//            },
//            dismissButton = {
//                Button(onClick = {
//                    //pickImageLauncher.launch("image/*")
//                    choosePicture()
//                    showDialog = false
//                }) {
//                    Text("Filesystem")
//                }
//            }
//        )
//    }
}