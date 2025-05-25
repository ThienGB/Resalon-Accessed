package com.example.reservationdemo.ui.module.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LoginViewModel = LoginViewModel(this)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "on-boarding",
                enterTransition = { fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                popEnterTransition = { fadeIn(animationSpec = tween(700)) },
                popExitTransition = { fadeOut(animationSpec = tween(700)) }
            ) {
                composable("on-boarding") { OnBoarding(navController) }
                composable("login") {
                    LoginUI(viewModel, navController)
                }
                composable("register") {
                    RegisterUI(viewModel, navController)
                }
                composable("forget-password")
                {
                    ForgetPassword(navController)
                }
            }
        }
    }
}

