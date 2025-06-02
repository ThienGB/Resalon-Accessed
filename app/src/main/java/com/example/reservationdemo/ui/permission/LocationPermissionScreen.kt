package com.example.reservationdemo.ui.permission

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
) {
    val locationPermission = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status) {
        when {
            locationPermission.status.isGranted -> {
                onPermissionGranted()
            }

            locationPermission.status.shouldShowRationale -> {
                // Người dùng từ chối nhưng có thể yêu cầu lại
                onPermissionDenied()
            }

            !locationPermission.status.isGranted && !locationPermission.status.shouldShowRationale -> {
                // Người dùng từ chối và chọn "Don't ask again" hoặc chưa cấp lần nào
                onPermissionDenied()
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermission.launchPermissionRequest()
    }
}
