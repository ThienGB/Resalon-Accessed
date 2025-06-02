package com.example.reservationdemo.ui.module.search

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reservationdemo.R
import com.example.reservationdemo.ui.components.CustomSearchBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.reservationdemo.data.api.model.SearchResultItem
import com.example.reservationdemo.helper.getCurrentLocation
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.module.home.CategoryItem
import com.example.reservationdemo.ui.module.home.Header
import com.example.reservationdemo.ui.module.main.HomeViewModel
import com.example.reservationdemo.ui.module.main.MainUserActivity
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun Search(
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current),
    navController: NavController = rememberNavController(),
    setSearchVisible: (Boolean) -> Unit = {},
    isFocused: Boolean = true
) {
    val searchLocation by viewModel.searchLocation.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var showLocationSearch by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val history by viewModel.searchHistory.collectAsState()
    var isShowHistory by remember { mutableStateOf(isFocused && history.isNotEmpty())}
    fun setSearchLocation(location: String) {
        viewModel.searchLocation.value = location
    }
    fun closeLocationSearch() {
        showLocationSearch = false
    }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val isSearchLoading by viewModel.isSearchLoading
    val searchResults = viewModel.searchResults.collectAsState()
    LaunchedEffect(isFocused) {
        if (isFocused)
            focusRequester.requestFocus()
    }
    LaunchedEffect(searchLocation) {
        if (searchText.length >= 2){
            viewModel.search(searchText)
        }
    }
    LaunchedEffect(searchResults.value) {
        isDropdownVisible = searchResults.value.isNotEmpty()
    }
    if (showLocationSearch){
        LocationSearch(
            viewModel = viewModel,
            onClose = { closeLocationSearch() },
            setSearchLocation = { location ->
            setSearchLocation(location) }
        )
    } else {
        Box (modifier = Modifier.fillMaxSize().background(Color.White).zIndex(100f)){
            Column (modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .background(Color.White)
                .verticalScroll(rememberScrollState())
            ) {
                Row(modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                setSearchVisible(false)
                            }
                    )
                }
                Column (modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(9.dp))) {
                    CustomSearchBar(placeholder = "Search for anything", text = searchText,
                        leadingIcon = R.drawable.ic_search_svg,
                        onTextChange = { newText -> viewModel.onSearchTextChange(newText) },
                        modifier = Modifier.then(if (isFocused) Modifier.focusRequester(focusRequester) else Modifier))
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (isSearchLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                if (isDropdownVisible) {
                    SearchDropdown(
                        results = searchResults.value,
                        onItemSelected = { selected ->
                            // Xử lý chọn item
                            Log.d("Search", "Selected: ${selected.name} (${selected.type})")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column (modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .clickableWithScale { showLocationSearch = true }) {
                    CustomSearchBar(placeholder = "Current location",
                        text = searchLocation, leadingIcon = R.drawable.ic_map,
                        onTextChange = { newText -> viewModel.searchLocation.value = newText},
                        isDisable = true)
                }
                Spacer(modifier = Modifier.height(30.dp))
                if (isShowHistory){
                    SearchCategorySlider(viewModel)
                    Spacer(modifier = Modifier.height(30.dp))
                    Column (modifier = Modifier.background(Color.White).padding(horizontal = 8.dp)) {
                        Row (verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Recent searches", fontSize = 20.sp, fontWeight = FontWeight(600))
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Clear", fontSize = 18.sp, fontWeight = FontWeight(500), color = colorResource(R.color.primary2),
                                modifier = Modifier.clickableWithScale{
                                    viewModel.clearHistory()
                                    isShowHistory = false
                                })
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        history.forEach { term ->
                            SearchHistoryItem(viewModel, term)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }else {
                    SearchCategory(viewModel)
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .zIndex(101f)
                    .border(1.dp, colorResource(R.color.colorSeparator)),
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(R.color.primary2))
                        .padding(vertical = 12.dp)
                        .clickableWithScale{
                            if (searchText != "")
                                viewModel.addSearchTerm(searchText)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
fun SearchCategory(viewModel: HomeViewModel){
    val categories by viewModel.categories.collectAsState()
    Column (modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Categories", fontSize = 18.sp, fontWeight = FontWeight(600))
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 5000.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categories.size) {index ->
                val category = categories.get(index)
                CategoryItem(category.name.toString(), category.image.toString())
            }
        }
    }
}

@Composable
fun SearchHistoryItem(
    viewModel: HomeViewModel,
    content: String = "Hair cut"
){
    Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        .clickableWithScale{
            viewModel.searchText.value = content
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
                painter = painterResource(R.drawable.ic_search_svg),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(R.color.primary2)),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = content, fontSize = 16.sp, fontWeight = FontWeight(600))
    }
}
@Composable
fun SearchCategorySlider(viewModel: HomeViewModel){
    val categories by viewModel.categories.collectAsState()
    Column {
        Text(text = "Categories", fontSize = 18.sp, fontWeight = FontWeight(600),
            modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 5000.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(categories.size) {index ->
                val category = categories[index]
                if (index == 0){
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                }
                Box() {
                    Image(painter =  rememberAsyncImagePainter(category.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(100.dp)
                            .width(200.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Text(text = category.name.toString(), fontSize = 16.sp, fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(10.dp)
                            .width(90.dp))
                }
                if (index == categories.size - 1){
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            }
        }
    }
}


@Composable
fun SearchDropdown(
    results: List<SearchResultItem>,
    onItemSelected: (SearchResultItem) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp, horizontal = 20.dp)
        .heightIn(max = 2000.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(results.size) { index ->
            val item = results[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!item.image.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column (verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(item.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    item.address?.let {
                        Text(it, fontSize = 13.sp, color = Color.LightGray)
                    }
                    Text(item.type.uppercase(), fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
