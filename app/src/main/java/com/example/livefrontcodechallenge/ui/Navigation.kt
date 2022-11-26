package com.example.livefrontcodechallenge.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.livefrontcodechallenge.Screen
import com.example.livefrontcodechallenge.view.apod.detail.ApodDetailView
import com.example.livefrontcodechallenge.view.apod.list.ApodListView

@Composable
fun Navigation() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = Screen.ApodListScreen.route) {
    composable(route = Screen.ApodListScreen.route) {
      ApodListView(navController = navController)
    }
    composable(
      route = Screen.ApodDetailScreen.route + "/{date}",
      arguments = listOf(
        navArgument("date") {
          type = NavType.StringType
        })
    ) { entry ->
      ApodDetailView(
        navController = navController,
        date = entry.arguments?.getString("date", "") ?: ""
      )
    }
  }
}