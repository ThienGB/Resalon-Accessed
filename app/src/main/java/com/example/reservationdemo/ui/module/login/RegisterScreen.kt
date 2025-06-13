package com.example.reservationdemo.ui.module.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reservationdemo.R
import com.example.reservationdemo.data.api.manager.ApiResult
import com.example.reservationdemo.ui.components.LoadingScreen
import com.example.reservationdemo.ui.components.RequiredOutlinedTextField
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterUI(
    navController: NavController = NavController(LocalContext.current)
) {
    val viewModel: LoginViewModel = koinViewModel()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var isLoading by remember { mutableStateOf(false) }
    fun setErrorMessage(message: String) {
        errorMessage = message
    }

    val context = LocalContext.current
    fun validateRegisterFields(): Boolean {
        if (fullName.isBlank()) {
            setErrorMessage("Họ tên không được để trống")
            return false
        }
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
        if (confirmPassword.isBlank()) {
            setErrorMessage("Vui lòng xác nhận mật khẩu")
            return false
        } else if (password != confirmPassword) {
            setErrorMessage("Mật khẩu xác nhận không khớp")
            return false
        }
        if (phone.isNotBlank() && !phone.matches(Regex("^[0-9]{9,15}$"))) {
            setErrorMessage("Số điện thoại không hợp lệ")
            return false
        }
        if (address.isBlank()) {
            setErrorMessage("Địa chỉ không được để trống")
            return false
        }
        setErrorMessage("")
        return true
    }

    val registerState by viewModel.registerResult.collectAsState()
    LaunchedEffect(registerState) {
        when (registerState) {
            is ApiResult.Loading -> {
                isLoading = true
            }
            is ApiResult.Success -> {
                Toast.makeText(context, "Registration successful, please login again. ", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                isLoading = false
            }
            is ApiResult.Error -> {
                Toast.makeText(context, "An error occurred, Registration failed.", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }
        viewModel.clearRegister()
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
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(25.dp)
                    .clickableWithScale {
                        navController.popBackStack()
                    }
            )
            Text(text = "Sign Up", modifier = Modifier
                .weight(1f)
                .padding(end = 25.dp), fontWeight = FontWeight(500),
                color = Color.Gray, fontSize = 18.sp, textAlign = TextAlign.Center)
        }
        Image(
            painter = painterResource(id = R.drawable.resalon_logo_name),
            contentDescription = "logo",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Crop
        )
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
                labelText = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                imeAction = ImeAction.Next
            )

            RequiredOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "Your email",
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
                labelText = "Phone number",
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                imeAction = ImeAction.Next
            )
            RequiredOutlinedTextField(
                value = address,
                onValueChange = { address = it },
                labelText = "Address",
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                imeAction = ImeAction.Next
            )
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

            RequiredOutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                labelText = "Confirm password",
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
                    .background(colorResource(R.color.primary2))
                    .padding(vertical = 12.dp)
                    .clickableWithScale {
                        if (validateRegisterFields()){
                            viewModel.register(fullName, email, password, phone, address)
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign Up",
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
                    .border(1.dp, colorResource(R.color.primary2), RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .clickableWithScale {
                        navController.popBackStack()
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Return to the login",
                    color = colorResource(R.color.primary2),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}