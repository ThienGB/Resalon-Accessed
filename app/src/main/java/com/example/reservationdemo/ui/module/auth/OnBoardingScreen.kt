package com.example.reservationdemo.ui.module.auth

import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reservationdemo.R
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import kotlin.math.absoluteValue

@Preview(showBackground = true)
@Composable
fun OnBoarding(
    navController: NavController = NavController(LocalContext.current),
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val onboardingImages = listOf(
        R.drawable.on_boarding_1,
        R.drawable.on_boarding_2,
        R.drawable.on_boarding_3,
        R.drawable.on_boarding_4
    )

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 200f
                )
            )
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val absPageOffset = pageOffset.absoluteValue
            Image(
                painter = painterResource(id = onboardingImages[page]),
                contentDescription = "Onboarding Image $page",
                modifier = Modifier.fillMaxSize()
                    .graphicsLayer {
                        // Rotate the page along the Y-axis for a flipping effect
                        rotationY = pageOffset * 90f // Rotate up to 90 degrees
                        // Adjust alpha to create a fade effect during flip
                        alpha = 1f - absPageOffset.coerceIn(0f, 1f) * 0.5f
                        // Adjust scale for a slight zoom effect
                        scaleX = 1f - absPageOffset * 0.1f
                        scaleY = 1f - absPageOffset * 0.1f
                        // Add camera distance for a more realistic 3D effect
                        cameraDistance = 8f
                    },
                contentScale = ContentScale.Crop
            )
        }
        Column(modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(30.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Find The Best Service",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "There are various services from salons that have become our partners.",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) colorResource(R.color.colorOrange) else Color.Gray
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Buttons
                Button(
                    onClick = { navController.navigate("login") },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.primary2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Get Started", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight(600))
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600),
                        color = colorResource(R.color.colorOrange),
                        modifier = Modifier.clickableWithScale{
                            navController.navigate("register")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

    }
}