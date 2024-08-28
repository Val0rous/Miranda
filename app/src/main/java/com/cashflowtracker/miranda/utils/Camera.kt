package com.cashflowtracker.miranda.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.saveProfilePicturePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

interface CameraLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}

@Composable
fun rememberCameraLauncher(): CameraLauncher {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageUri = remember {
        val imageFile = File.createTempFile("tmp_image", ".jpg", context.externalCacheDir)
        FileProvider.getUriForFile(context, context.packageName + ".provider", imageFile)
    }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (pictureTaken) {
                capturedImageUri = imageUri
                val imagePath =
                    saveImageToStorage(capturedImageUri, context.applicationContext.contentResolver)

                coroutineScope.launch(Dispatchers.IO) {
                    context.saveProfilePicturePath(imagePath)
                }
            }
        }

    val cameraLauncher by remember {
        derivedStateOf {
            object : CameraLauncher {
                override val capturedImageUri = capturedImageUri
                override fun captureImage() = cameraActivityLauncher.launch((imageUri))
            }
        }
    }
    return cameraLauncher
}
