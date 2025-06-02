package com.example.reservationdemo.ui.module.home

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.reservationdemo.R
import com.example.reservationdemo.data.api.model.Rating
import com.example.reservationdemo.helper.getCurrentLocation
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.module.login.LoginActivity
import com.example.reservationdemo.ui.module.main.HomeViewModel
import com.example.reservationdemo.ui.module.main.MainUserActivity
import com.example.reservationdemo.ui.module.search.LocationSearch
import com.example.reservationdemo.ui.module.search.Search
import com.example.reservationdemo.ui.permission.LocationPermissionScreen
import com.google.android.gms.location.LocationServices
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.max

@Composable
fun Home(
    viewModel: HomeViewModel,
    navController: NavController = rememberNavController()
) {
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
    var showLocationSearch by remember { mutableStateOf(false) }
    var showAllCategories by remember { mutableStateOf(false) }
    var showSidebar by remember { mutableStateOf(false) }
    fun showSidebar() {
        showSidebar = true
    }
    fun setLocation(location: String){
        viewModel.location.value = location
    }
    fun closeLocationSearch() {
        showLocationSearch = false
    }
    fun openLocationSearch() {
        showLocationSearch = true
    }
    fun setSearchVisible(status: Boolean){
        isSearchVisible = status
    }
    fun setCategoryVisible(status: Boolean){
        showAllCategories = status
    }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    LocationPermissionScreen(
        onPermissionGranted = {isPermissionGranted = true},
        onPermissionDenied = {
            Toast.makeText(context, "Resalon cannot access your location", Toast.LENGTH_SHORT).show()
            viewModel.location.value = viewModel.user.value.address.toString()
            viewModel.searchLocation.value = viewModel.user.value.address.toString()
        }
    )
    LaunchedEffect(Unit) {
        viewModel.fetchAllData()
    }
    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            getCurrentLocation(
                context = context,
                fusedLocationClient = fusedLocationClient,
                onLocationReceived = { city ->
                    viewModel.searchLocation.value = city
                    viewModel.location.value = city
                },
                onError = { error ->
                    Log.e("LocationError", error.message ?: "Unknown error")
                }
            )
        }
    }

    if (isSearchVisible) {
        Search(viewModel, navController, { status ->
            setSearchVisible(status) } )
    } else if (showLocationSearch){
        LocationSearch(
            viewModel,
            onClose = { closeLocationSearch() },
            setSearchLocation = { location ->
                setLocation(location) }
        )
    } else if (showAllCategories){
        Search(viewModel, navController, { status ->
            setCategoryVisible(status) }, false )
    }
    LandingPage(
        viewModel, navController,
        { status -> setSearchVisible(status) }, { openLocationSearch() },
        { status -> setCategoryVisible(status) },
        { showSidebar() }
    )
    val activity = LocalActivity.current
    val items = listOf(
        SidebarItem("Profile", R.drawable.ic_profile_color,{}),
        SidebarItem("LogOut", R.drawable.ic_logout_color,{
            viewModel.clearToken()
            activity?.startActivity(Intent(activity, LoginActivity::class.java))
        })
    )
    RightSideBar(
        viewModel = viewModel,
        isVisible = showSidebar,
        onDismiss = { showSidebar = false },
        items = items
    )

}
@Composable
fun LandingPage(
    viewModel: HomeViewModel,
    navController: NavController,
    setSearchVisible: (Boolean) -> Unit,
    openLocationSearch: () -> Unit,
    setCategotyVisible: (Boolean) -> Unit,
    showSidebar: () -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val searchLocation by viewModel.searchLocation.collectAsState()
    val scrollState = rememberScrollState()
    val isCollapsed by remember {
        derivedStateOf { scrollState.value > 5 }
    }
    val isCollapsedSearch by remember {
        derivedStateOf { scrollState.value > 300 }
    }
    val density = LocalDensity.current
    val offsetPx = with(density) { 50.dp.toPx() }
    var tabRowY by remember { mutableStateOf(0f) }
    var tabRowHeight by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.img_bg_resalon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            )
            Header(showSidebar)
        }
        Column(
            modifier = Modifier
                .offset {
                    val offset =
                        max(a = -50f, b = scrollState.value - tabRowY + tabRowHeight - offsetPx)
                    IntOffset(x = 0, y = offset.toInt())
                }
                .zIndex(10f)
                .clip(RoundedCornerShape(9.dp))
                .clickableWithScale {
                    setSearchVisible(true)
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 3.dp)
                    .onGloballyPositioned { coordinates ->
                        if (tabRowY == 0f) {
                            tabRowY = coordinates.positionInWindow().y
                            tabRowHeight = coordinates.size.height.toFloat()
                        }
                    }
                    .then(
                        if (isCollapsedSearch) Modifier
                            .graphicsLayer(alpha = 0.8f)
                            .wrapContentWidth() else Modifier.fillMaxWidth()
                    )
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(9.dp),
                        clip = false
                    )
                    .background(Color.White, shape = RoundedCornerShape(9.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(9.dp))
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_search_svg),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.LightGray),
                        modifier = Modifier.size(25.dp)
                    )
                    if (!isCollapsedSearch)
                        Spacer(modifier = Modifier.width(10.dp))
                    AnimatedContent(
                        targetState = Pair(isCollapsed, isCollapsedSearch),
                        label = "SearchTextTransition",
                        transitionSpec = {
                            if (targetState.first) {
                                (fadeIn(tween(durationMillis = 300)) + slideInVertically(tween(durationMillis = 300)) { -it / 2 })
                                    .togetherWith(fadeOut(tween(durationMillis = 300)) + slideOutVertically(tween(durationMillis = 300)) { -it / 2 })
                            } else {
                                (fadeIn(tween(durationMillis = 300)) + slideInVertically(tween(durationMillis = 300)) { it / 2 })
                                    .togetherWith(fadeOut(tween(durationMillis = 300)) + slideOutVertically(tween(durationMillis = 300)) { it / 2 })
                            }.using(SizeTransform(clip = true))
                        }
                    ) { (collapsed, collapsedSearch) ->
                        if (!collapsedSearch) {
                            if (!collapsed) {
                                Column {
                                    Text(
                                        text = searchText,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = searchLocation,
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = searchText,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "  •  ",
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = searchLocation,
                                        color = Color.LightGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        } else {
                            Box {}
                        }
                    }
                }
            }
        }
        Recommended(viewModel)
        Spacer(modifier = Modifier.height(50.dp))
        PopularServicesInLocation(viewModel,openLocationSearch)
        Spacer(modifier = Modifier.height(50.dp))
        Category(viewModel, setCategotyVisible)
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun Header(
    showSidebar: () -> Unit
) {
    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)){
            Image(
                painter = painterResource(R.drawable.resalon_logo),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Resalon", fontSize = 20.sp, fontWeight = FontWeight(700),
                color= colorResource(R.color.primary2))
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .border(
                    width = 0.5.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .padding(1.dp)
                .clickableWithScale {
                    showSidebar()
                }
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
    }
}

@Composable
fun Category(
    viewModel: HomeViewModel,
    setCategotyVisible: (Boolean) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    Column() {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickableWithScale{
                    setCategotyVisible(true)
                }
            ){
                Text(
                    text = "See all",
                    fontSize = 16.sp,
                    color = colorResource(R.color.primary2),
                    fontWeight = FontWeight(400),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colorResource(R.color.primary2),
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(15.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .heightIn(max = 5000.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categories.take(10).size) {index ->
                val category = categories.get(index)
                CategoryItem(category.name.toString(), category.image.toString())
            }
        }
    }
}

@Composable
fun CategorySlider(
    viewModel: HomeViewModel,
    setCategotyVisible: (Boolean) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    Column() {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickableWithScale{
                    setCategotyVisible(true)
                }
            ){
                Text(
                    text = "See all",
                    fontSize = 16.sp,
                    color = colorResource(R.color.primary2),
                    fontWeight = FontWeight(400),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colorResource(R.color.primary2),
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(15.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        val chunkedItems = categories.chunked(4)
        val pagerState = rememberPagerState(pageCount = { chunkedItems.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 20.dp,
            contentPadding = PaddingValues(horizontal = 20.dp),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
            ),
        ) { page ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.clipToBounds()
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    chunkedItems[page].take(2).forEach {item ->
                        CategoryItem(item.name.toString(), item.image.toString())
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    chunkedItems[page].drop(2).take(2).forEach {item ->
                        CategoryItem(item.name.toString(), item.image.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Recommended(
    viewModel: HomeViewModel
){
    Spacer(modifier = Modifier.height(10.dp))
    PopularSalons(viewModel)
    Spacer(modifier = Modifier.height(40.dp))
    PopularStaffs(viewModel)
//    Spacer(modifier = Modifier.height(40.dp))
//    PopularServices(viewModel)
}

@Composable
fun PopularSalons(viewModel: HomeViewModel){
    val businesses by viewModel.businesses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Popular salons", fontSize = 18.sp, fontWeight = FontWeight(600),)
    }
    Spacer(modifier = Modifier.height(15.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp),){
        items(if (isLoading) 5 else businesses.size, key = { it }){index->
            if (isLoading) {
                if (index == 0) {
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                }
                RecommendedItemSkeleton()
                if (index == 4) { // Last skeleton
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            } else {
                val business = businesses[index]
                if (index == 0) {
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                }
                RecommendedItem(
                    viewModel, business.id.toString(), "business",
                    title = business.name.toString(),
                    location = business.description.toString(),
                    image = business.image.toString(),
                    ratings = business.ratings
                )
                if (index == businesses.size - 1) {
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            }
        }
    }
}

@Composable
fun PopularStaffs(viewModel: HomeViewModel){
    val individuals by viewModel.individuals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Popular staffs", fontSize = 18.sp, fontWeight = FontWeight(600),)
    }
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)){
        items(if (isLoading) 5 else individuals.size, key = { it }){index->
            if (isLoading) {
                if (index == 0) {
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                }
                RecommendedItemSkeleton()
                if (index == 4) { // Last skeleton
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            } else {

                val individual = individuals[index]
                if (index == 0)
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                RecommendedItem(viewModel, individual.id.toString(), "individual", individual.name,
                    individual.description.toString(), individual.image.toString(), individual.ratings)
                if (index == individuals.size - 1)
                    Spacer(modifier = Modifier.padding(end = 20.dp))
            }

        }
    }

}


@Composable
fun PopularServicesInLocation(
    viewModel: HomeViewModel,
    openLocationSearch: () -> Unit
){
    val location by viewModel.location.collectAsState()
    LaunchedEffect(location) {
        viewModel.fetchServiceByCity()
    }
    val servicesByCity by viewModel.servicesByCity.collectAsState()
    val services = servicesByCity.firstOrNull()?.services ?: emptyList()
    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Popular services in", fontSize = 18.sp, fontWeight = FontWeight(600),)
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .clickableWithScale {
                    openLocationSearch()
                }
        ){
            Text(text = location, fontSize = 18.sp, fontWeight = FontWeight(600),
                color = colorResource(R.color.primary2))
            Spacer(modifier = Modifier.width(5.dp))
            Image(painter =  painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(colorResource(R.color.primary2)),
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    if (services.isEmpty()){
        Text(text = "No services found", fontSize = 18.sp, fontWeight = FontWeight(600),
            color = colorResource(R.color.primary2),
            modifier = Modifier.padding(horizontal = 20.dp))
    }else
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ){
        items(services.size){index ->
            val service = services.get(index)
            if (index == 0)
                Spacer(modifier = Modifier.padding(start = 20.dp))
            ServiceItem(service.serviceTitle, service.serviceImage.toString())
            if (index == services.size - 1)
                Spacer(modifier = Modifier.padding(end = 20.dp))
        }
    }
}

@Composable
fun RecommendedItem(
    viewModel: HomeViewModel,
    id: String = "",
    type: String = "",
    title: String = "Optima Beauty Clinic",
    location: String = "Salon • Ringvagen St, 74 • 6969",
    image: String = "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2024/04/anh-ha-noi.jpg",
    ratings: List<Rating> = emptyList(),
){
    val user by viewModel.user.collectAsState()
    fun calculateAverageRating(): Double {
        if (ratings.isEmpty()) return 0.0
        val sum = ratings.sumOf { it.rate }
        val average = sum.toDouble() / ratings.size
        return "%.1f".format(Locale.US, average).toDouble()
    }
    var favoriteState by remember { mutableStateOf(false) }
    favoriteState = if (type == "individual")
        user.favorite?.individual?.contains(id) == true
    else
        user.favorite?.business?.contains(id) == true
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var isFavorite by rememberSaveable { mutableStateOf(favoriteState) }
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    fun onLikeClick() {
        if (!isFavorite) {
            viewModel.addToFavorite(type, id)
        } else {
            viewModel.removeFromFavorite(type, id)
        }
        isFavorite = !isFavorite

        coroutineScope.launch {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 150)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 150)
            )
        }
    }
    Column {
        Column(modifier = Modifier
            .width(screenWidth * 0.7f)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(image), // Thay bằng resource hình ảnh của bạn
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(99.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = colorResource(id = R.color.first_ranking),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = calculateAverageRating().toString(), fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
                Icon(
                    painter = painterResource(if (isFavorite) R.drawable.ic_favorited else R.drawable.ic_favorite),
                    contentDescription = "Favorite",
                    tint =if (isFavorite) Color.Red else Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value
                        )
                        .clickable { onLikeClick() }
                )
                Column (
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                        .clip(RoundedCornerShape(99.dp))
                        .background(colorResource(R.color.primary4))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(text = "Recommended", color = colorResource(R.color.primary2), fontSize = 13.sp, fontWeight = FontWeight(600))
                }
            }
        }
        Column(modifier = Modifier
            .padding(top = 8.dp)
            .width(screenWidth * 0.7f),
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = location,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight(400),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@Composable
fun RecommendedItemSkeleton() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column {
        Column(
            modifier = Modifier
                .width(screenWidth * 0.7f)
                .clip(RoundedCornerShape(8.dp))
                .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .shimmer(shimmer)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .shimmer(shimmer)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .size(width = 60.dp, height = 24.dp)
                )
                // Favorite icon placeholder
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .shimmer(shimmer)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
                // Recommended badge placeholder
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .shimmer(shimmer)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .size(width = 100.dp, height = 20.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .width(screenWidth * 0.7f)
        ) {
            // Title placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(shimmer)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Location placeholder (2 lines)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(shimmer)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(shimmer)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun CategoryItem(
    title: String = "NailCare",
    image: String = ""
) {
    Box() {
        Image(painter =  rememberAsyncImagePainter(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
        )
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(10.dp)
                .width(90.dp))
    }
}

@Composable
fun ServiceItem(
    title: String = "Women's haircut",
    image: String = ""
) {
    Box() {
        Image(painter =  rememberAsyncImagePainter(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(10.dp)
                .width(80.dp))
    }
}

@Composable
fun RightSideBar(
    viewModel: HomeViewModel,
    isVisible: Boolean = true,
    items: List<SidebarItem> = emptyList(),
    onDismiss: () -> Unit = {},
) {
    val user by viewModel.user.collectAsState()
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )+ fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )+ fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismiss()
                }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.75f)
                        .background(Color.White)
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .border(
                                width = 0.5.dp,
                                color = colorResource(R.color.primary2),
                                shape = CircleShape
                            )
                            .padding(2.dp)
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = user.name.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.email.toString(), fontSize = 16.sp, fontWeight = FontWeight(500), color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .heightIn(max = 5000.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items.size) {index ->
                            val item = items.get(index)
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clickableWithScale {
                                    item.onClick()
                                },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Image(
                                    painter = painterResource(item.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = item.label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }


}

data class SidebarItem(
    val label: String,
    val icon: Int,
    val onClick: () -> Unit
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Banner(
    navController: NavController,
    setSearchVisible: (Boolean) -> Unit,
    scrollState: ScrollState
) {
    val isCollapsed by remember {
        derivedStateOf { scrollState.value > 5 }
    }
    val density = LocalDensity.current
    val offsetPx = with(density) { 20.dp.toPx() }
    var tabRowY by remember { mutableStateOf(0f) }
    var tabRowHeight by remember { mutableStateOf(0f) }

    Column (modifier = Modifier
        .padding(horizontal = 20.dp)
        .offset(y = (-26).dp)
        .clip(RoundedCornerShape(9.dp))
        .clickableWithScale {
            setSearchVisible(true)
        }
        .onGloballyPositioned { coordinates ->
            if (tabRowY == 0f) {
                tabRowY = coordinates.positionInWindow().y
                tabRowHeight = coordinates.size.height.toFloat()
            }
        }
        .graphicsLayer {
            translationY =
                max(a = 0f, b = scrollState.value - tabRowY + tabRowHeight - offsetPx)
        }
        .zIndex(10f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(9.dp),
                    clip = false
                )
                .background(Color.White, shape = RoundedCornerShape(9.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(9.dp))
                .padding(vertical = 10.dp, horizontal = 12.dp)

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_search_svg),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(25.dp))

                AnimatedContent(
                    targetState = isCollapsed,
                    label = "SearchTextTransition",
                    transitionSpec = {
                        (fadeIn(tween(200)) + slideInVertically { it }).togetherWith(fadeOut(tween(200)) + slideOutVertically { -it })
                    }
                ) { collapsed ->
                    if (!collapsed) {
                        Column {
                            Text(
                                text = "Search for anything",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Ho Chi Minh",
                                color = Color.LightGray,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        Row {
                            Text(
                                text = "Search for anything",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "  •  ",
                                color = Color.LightGray,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Ho Chi Minh",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun PopularServices(viewModel: HomeViewModel){
    val services by viewModel.services.collectAsState()
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Popular services", fontSize = 18.sp, fontWeight = FontWeight(600),)
    }
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)){
        items(services.size){index->
            val service = services.get(index)
            if (index == 0)
                Spacer(modifier = Modifier.padding(start = 20.dp))
            //RecommendedItem(service.title.toString(), "", service.image.toString())
        }
    }

}
