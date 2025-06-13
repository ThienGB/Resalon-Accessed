package com.example.reservationdemo.data.api.model

data class SearchResponse(
    val result:  List<SearchResultItem>? = null
)
data class SearchResultItem(
    val id: String = "",
    val name: String = "",
    val image: String? = "",
    val type: String= "",
    val address: String? = null
)