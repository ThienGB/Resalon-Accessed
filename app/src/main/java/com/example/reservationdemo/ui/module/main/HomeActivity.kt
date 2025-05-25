package com.example.reservationdemo.ui.module.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reservationdemo.R
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.font.VCompassTheme
import com.example.reservationdemo.ui.module.home.Home
import com.example.reservationdemo.ui.module.search.Search


class MainUserActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: HomeViewModel = HomeViewModel(this)
        setContent {
            VCompassTheme {
                MainScreen(viewModel)
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: HomeViewModel
) {
    val isShowBottomBar = false
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = currentBackStackEntry?.destination?.route ?: "home"
    Scaffold(
        bottomBar = {
            if (isShowBottomBar)
                CustomBottomBar(viewModel, navController, selectedRoute )
        }
    ) { padding ->
        NavHostGraph(
            viewModel,
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
@Composable
fun CustomBottomBar(
    viewModel: HomeViewModel,
    navController: NavController = rememberNavController(),
    selectedRoute: String = "home"
) {
    val curentTab by remember {mutableStateOf(selectedRoute)}
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(40.dp)
    ) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                BottomBarItem("home", painterResource(id = R.drawable.ic_house_solid), painterResource(id = R.drawable.ic_house), "Trang chủ"),
                BottomBarItem("search", painterResource(id = R.drawable.ic_search_category_solid), painterResource(id = R.drawable.ic_search_category), "Lịch sử"),
                BottomBarItem("explore", painterResource(id = R.drawable.ic_compass_solid), painterResource(id = R.drawable.ic_compass), "Khám phá"),
                BottomBarItem("profile", painterResource(id = R.drawable.ic_profile_shape_solid), painterResource(id = R.drawable.ic_proflie_shape), "Cá nhân")
            )

            items.forEach { item ->
                val isSelected = curentTab == item.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            color = if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .clickableWithScale {
                            navController.navigate(item.route)
                        }
                        .padding(vertical = 5.dp)
                ) {
                    Icon(
                        painter = if (isSelected) item.selectedIcon else item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) colorResource(R.color.primary) else if (item.type == "main") colorResource(id = R.color.lightDarkBlue) else Color.Gray,
                        modifier = Modifier.size(if (item.type == "main") 28.dp else 22.dp)
                    )
                    Text(text = item.label, color = if (isSelected) colorResource(R.color.primary) else if (item.type == "main") colorResource(id = R.color.lightDarkBlue) else Color.Gray)
                }
            }
        }
    }
}

data class BottomBarItem(
    val route: String,
    val icon: Painter,
    val selectedIcon: Painter,
    val label: String,
    val type : String = "normal"
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostGraph(
    viewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier
)
{
    NavHost(navController = navController, startDestination = "home",
        enterTransition = { fadeIn(animationSpec = tween(1000)) },
        exitTransition = { fadeOut(animationSpec = tween(1000)) },
        popEnterTransition = { fadeIn(animationSpec = tween(1000)) },
        popExitTransition = { fadeOut(animationSpec = tween(1000)) }
    ) {
        composable("home") { Home(viewModel, navController) }
    }
}