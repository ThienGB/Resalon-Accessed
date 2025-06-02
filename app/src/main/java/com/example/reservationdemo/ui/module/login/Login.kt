package com.example.reservationdemo.ui.module.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reservationdemo.R
import com.example.reservationdemo.helper.getCurrentLocation
import com.example.reservationdemo.ui.components.LoadingScreen
import com.example.reservationdemo.ui.components.RequiredOutlinedTextField
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.module.main.MainUserActivity

@Composable
fun LoginUI(
    viewModel: LoginViewModel,
    navController: NavController = NavController(LocalContext.current),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val activity = LocalActivity.current
    val loginResult by viewModel.loginResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var errorMessage by remember { mutableStateOf("") }
    fun setErrorMessage(message: String) {
        errorMessage = message
    }
    fun validateRegisterFields(): Boolean {
        if (email.isBlank()) {
            setErrorMessage("Email không được để trống")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setErrorMessage("Email không hợp lệ")
            return false
        }
        if (password.isBlank()) {
            setErrorMessage("Mật khẩu không được để trống")
            return false
        } else if (password.length < 6) {
            setErrorMessage("Mật khẩu phải có ít nhất 6 ký tự")
            return false
        }
        setErrorMessage("")
        return true
    }
    val userToken by viewModel.userToken.collectAsState(null)
    LaunchedEffect(userToken) {
        if (!userToken.isNullOrEmpty()) {
            val expired = viewModel.isTokenExpired(userToken!!)
            if (!expired) {
                activity?.startActivity(Intent(activity, MainUserActivity::class.java))
                activity?.finish()
            } else {
                viewModel.clearToken()
            }
        } else {
            // Chưa có token, show login screen
        }
    }
    LaunchedEffect(loginResult) {
        if (loginResult.user != null  ) {
            val token = loginResult.access_token ?: ""
            val userId = loginResult.user?.name ?: ""
            viewModel.saveLoginInfo(token, userId)
        } else if (loginResult.status == 400) {
            Toast.makeText(context, "Wronvg email or password", Toast.LENGTH_SHORT).show()
            viewModel.clearLogin()
        }
    }
    if (isLoading) {
        LoadingScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.resalon_logo_name),
            contentDescription = "logo",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(text = "Please log in to continue",
                color = Color.Gray,
                fontSize = 14.sp,)

            Spacer(modifier = Modifier.height(16.dp))

            RequiredOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "Your email",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(8.dp))
            RequiredOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                labelText = "Password",
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

            TextButton(
                onClick = {  navController.navigate("forget-password")  },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Forgot password", color = colorResource(R.color.primary2))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.primary2))
                    .padding(vertical = 12.dp)
                    .clickableWithScale() {
                        if (!validateRegisterFields()) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }else {
                            viewModel.login(email, password)
                        }

                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, colorResource(R.color.primary2), RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .clickableWithScale {
                        navController.navigate("register")
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Create new account",
                    color = colorResource(R.color.primary2),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


