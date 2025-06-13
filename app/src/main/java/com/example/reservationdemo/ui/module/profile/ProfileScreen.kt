package com.example.reservationdemo.ui.module.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reservationdemo.R
import com.example.reservationdemo.ui.components.RequiredOutlinedTextField


@Composable
fun Profile(
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f),
            contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(R.drawable.bg_profile_blue),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_hue),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Hoàng Công Thiện",
                        color = colorResource(R.color.blue),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "thien*****.com",
                        color = colorResource(R.color.blue),
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_proflie_shape),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(R.color.blue)),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Thông tin cá nhân",
                    fontSize = 16.sp,
                    color = colorResource(R.color.blue),
                    fontWeight = FontWeight(500))
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_log_out),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Red),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Đăng xuất",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight(500))
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun EditProfile(
    navController: NavController = NavController(LocalContext.current)
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(25.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(text = "Thông tin cá nhân", modifier = Modifier.weight(1f).padding(end = 25.dp), fontWeight = FontWeight(500),
                color = Color.Gray, fontSize = 18.sp, textAlign = TextAlign.Center)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_hue),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Hoàng Công Thiện",
                    color = colorResource(R.color.blue),
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "thien*****.com",
                    color = colorResource(R.color.blue),
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RequiredOutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                labelText = "Họ và tên",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                imeAction = ImeAction.Next
            )

            RequiredOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "Email của bạn",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                imeAction = ImeAction.Next,
                isEmail = email.length < 6 || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )

            RequiredOutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    if (input.length <= 11 && input.all { it.isDigit() }) {
                        phone = input
                    }
                },
                keyboardType = KeyboardType.Number,
                labelText = "Số điện thoại",
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                imeAction = ImeAction.Next
            )
            RequiredOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                labelText = "Mật khẩu",
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password Icon")
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible) R.drawable.passwordshow else R.drawable.passwordhidden
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPasswordVisible  = isPasswordVisible
            )

            RequiredOutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                labelText = "Nhập lại mật kẩu",
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password Icon")
                },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (isConfirmPasswordVisible) R.drawable.passwordshow else R.drawable.passwordhidden
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPasswordVisible  = isConfirmPasswordVisible,
                rePasswordError = password != confirmPassword
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.primary))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đăng ký",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, colorResource(R.color.primary), RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .clickable {
                        navController.popBackStack()
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quay lại trang đăng nhập",
                    color = colorResource(R.color.primary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


