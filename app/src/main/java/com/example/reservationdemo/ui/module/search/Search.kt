package com.example.reservationdemo.ui.module.search

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.zIndex
import com.example.reservationdemo.ui.custom_property.clickableWithScale
import com.example.reservationdemo.ui.module.home.CategoryItem
import com.example.reservationdemo.ui.module.home.Header
import com.example.reservationdemo.ui.module.main.HomeViewModel
import com.example.reservationdemo.ui.module.main.MainUserActivity
import kotlinx.coroutines.delay

@Composable
fun Search(
    viewModel: HomeViewModel,
    navController: NavController = rememberNavController(),
    setSearchVisible: (Boolean) -> Unit = {},
    isFocused: Boolean = true
) {
    val searchLocation by viewModel.searchLocation.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var showLocationSearch by remember { mutableStateOf(false) }
    fun setSearchLocation(location: String) {
        viewModel.searchLocation.value = location
    }
    fun closeLocationSearch() {
        showLocationSearch = false
    }

    LaunchedEffect(isFocused) {
        if (isFocused)
            focusRequester.requestFocus()
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
                        onTextChange = { newText -> viewModel.searchText.value = newText},
                        modifier = Modifier.then(if (isFocused) Modifier.focusRequester(focusRequester) else Modifier))
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
                SearchCategory(viewModel)
                Spacer(modifier = Modifier.height(80.dp))
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
                        .clickableWithScale(){

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
