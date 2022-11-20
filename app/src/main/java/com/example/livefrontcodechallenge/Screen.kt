package com.example.livefrontcodechallenge

sealed class Screen(val route: String) {
  object ApodListScreen : Screen("apod_list")
  object ApodDetailScreen : Screen("apod_detail")
}