package com.example.wheather_appkotlin

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)