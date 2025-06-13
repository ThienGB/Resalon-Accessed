package com.example.reservationdemo.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

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
