package com.example.reservationdemo.ui.module.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.reservationdemo.R
import com.example.reservationdemo.helper.getCurrentLocation
import com.example.reservationdemo.ui.components.CustomSearchBar
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.module.main.HomeViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun LocationSearch(
    viewModel: HomeViewModel,
    onClose: () -> Unit,
    setSearchLocation: (String) -> Unit
){
    val locations = listOf(
        "Ho Chi Minh" to "Tan Binh, Ho Chi Minh",
        "Ha Noi" to "Ba Dinh, Ha Noi",
        "Da Nang" to "Hai Chau, Da Nang",
        "Hai Phong" to "Le Chan, Hai Phong",
        "Can Tho" to "Ninh Kieu, Can Tho",
        "An Giang" to "Long Xuyen, An Giang",
        "Binh Duong" to "Thu Dau Mot, Binh Duong",
        "Khanh Hoa" to "Nha Trang, Khanh Hoa",
        "Lam Dong" to "Da Lat, Lam Dong",
        "Quang Ninh" to "Ha Long, Quang Ninh"
    )
    var searchLocation by remember { mutableStateOf("") }
    val filteredLocations = locations.filter { (province, address) ->
        province.contains(searchLocation, ignoreCase = true) ||
                address.contains(searchLocation, ignoreCase = true)
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column (modifier = Modifier.fillMaxSize()
        .pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
        .background(Color.White)
        .zIndex(102f)
        .verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Start) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                modifier = Modifier.size(25.dp).clickable {
                    onClose()
                }
            )
            Text(text = "Location", fontSize = 20.sp, fontWeight = FontWeight(700),
                modifier = Modifier.weight(1f).padding(end = 20.dp), textAlign = TextAlign.Center)
        }

        Column (modifier = Modifier.padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(9.dp))) {
            CustomSearchBar(placeholder = "Current location",
                text = searchLocation, leadingIcon = R.drawable.ic_map,
                onTextChange = { newText -> searchLocation = newText}, modifier = Modifier.focusRequester(focusRequester))
        }
        Spacer(modifier = Modifier.height(30.dp))
        if (searchLocation == "" || filteredLocations.isEmpty()){
            Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                .clickableWithScale{
                    getCurrentLocation(
                        context = context,
                        fusedLocationClient = fusedLocationClient,
                        onLocationReceived = { city ->
                            setSearchLocation(city)
                            onClose()
                        },
                        onError = { error ->
                            Log.e("LocationError", error.message ?: "Unknown error")
                        }
                    )
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = colorResource(R.color.primary4),
                            shape = CircleShape
                        )
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_location),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorResource(R.color.primary2)),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = "Current location", fontSize = 18.sp, fontWeight = FontWeight(500))
            }
        }else {
            filteredLocations.forEach { item ->
                LocationSearchItem(province = item.first, address = item.second, onClick = setSearchLocation, onClose = onClose)
            }
        }

    }
}
@Composable
fun LocationSearchItem(
    province: String = "Ho Chi Minh",
    address: String = "Tan Binh, Ho Chi Minh",
    onClick: (String) -> Unit,
    onClose: () -> Unit
){
    Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        .clickable{
            onClick(province)
            onClose()
        },
        verticalAlignment = Alignment.CenterVertically,){
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = colorResource(R.color.colorSeparator),
                    shape = CircleShape
                )
                .padding(9.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_aim),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(R.color.black_40)),
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = province, fontSize = 18.sp, fontWeight = FontWeight(500))
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = address, fontSize = 16.sp, fontWeight = FontWeight(400), color = Color.Gray)
        }
    }
}