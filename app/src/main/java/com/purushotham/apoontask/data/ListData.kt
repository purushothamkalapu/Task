package com.purushotham.apoontask.data

data class ListData(
    val clouds: CloudsX,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: Rain,
    val sys: SysX,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: WindX
)