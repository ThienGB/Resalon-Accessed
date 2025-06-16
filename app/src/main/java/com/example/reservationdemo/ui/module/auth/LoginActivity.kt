package com.example.reservationdemo.ui.module.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reservationdemo.ui.components.LoadingScreen
import org.koin.compose.viewmodel.koinViewModel

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: LoginViewModel = koinViewModel()
            val isFirst by viewModel.isFirst.collectAsState(null)
            if (isFirst != null){
                NavHost(navController = navController, startDestination = if (isFirst == true) "on-boarding" else "login",
                    enterTransition = { fadeIn(animationSpec = tween(700)) },
                    exitTransition = { fadeOut(animationSpec = tween(700)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(700)) },
                    popExitTransition = { fadeOut(animationSpec = tween(700)) }
                ) {
                    composable("on-boarding") { OnBoarding(navController) }
                    composable("login") {
                        LoginUI(navController)
                    }
                    composable("register") {
                        RegisterUI(navController)
                    }
                    composable("forget-password")
                    {
                        ForgetPassword(navController)
                    }
                }
            }else {
                LoadingScreen()
            }

        }
    }
}

