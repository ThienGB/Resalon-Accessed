package com.example.reservationdemo.ui.permission

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
    modifier: Modifier = Modifier,
    fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current),
    onLocationReceived: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val locationPermission = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    var locationText by remember { mutableStateOf("Chưa có dữ liệu vị trí") }
    var isLoading by remember { mutableStateOf(false) }

    // Gửi yêu cầu khi lần đầu
    LaunchedEffect(Unit) {
        locationPermission.launchPermissionRequest()
    }

    // Giao diện
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF154B51))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vị trí hiện tại",
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color(0xFF154B51)),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    locationPermission.status.isGranted -> {
                        Button(
                            onClick = {
                                isLoading = true
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location ->
                                        isLoading = false
                                        if (location != null) {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                try {
                                                    val geocoder = Geocoder(context, Locale.getDefault())
                                                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                                    val city = if (!addresses.isNullOrEmpty()) {
                                                        addresses[0].locality ?: addresses[0].adminArea ?: "City unknown"
                                                    } else {
                                                        "City unknown"
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        onLocationReceived(city)
                                                    }
                                                } catch (e: IOException) {
                                                    withContext(Dispatchers.Main) {

                                                    }
                                                }
                                            }
                                        } else {
                                            locationText = "Không lấy được vị trí."
                                        }
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        locationText = "Lỗi khi lấy vị trí: ${it.message}"
                                    }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF154B51))
                        ) {
                            Text("Lấy vị trí", color = Color.White)
                        }

                        if (isLoading) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(color = Color(0xFF154B51))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = locationText)
                    }

                    locationPermission.status.shouldShowRationale -> {
                        Text("Ứng dụng cần quyền vị trí để hoạt động.", color = Color.Red)
                    }

                    else -> {
                        Text("Quyền vị trí chưa được cấp hoặc bị từ chối.", color = Color.Red)
                    }
                }
            }
        }
    }
}
