package com.purushotham.apoontask.data

data class ForcastData(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ListData>,
    val message: Int
)