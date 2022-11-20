package com.example.livefrontcodechallenge.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.livefrontcodechallenge.R

@Composable
fun AppBar(
  navController: NavController,
  title: String = stringResource(id = R.string.app_name),
  content: @Composable() () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          if (navController.previousBackStackEntry != null) {
            IconButton(onClick = {
              navController.navigateUp()
            }) {
              Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
              )
            }
          }
        },
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
      )
    }
  ) { padding ->
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
        .padding(padding)
    ) {
      content()
    }
  }
}