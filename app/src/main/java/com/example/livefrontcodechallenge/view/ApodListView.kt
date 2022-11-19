package com.example.livefrontcodechallenge.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodMediaType
import com.example.livefrontcodechallenge.data.ApodModel

@Composable
fun ApodListView(models: List<ApodModel>) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
      )
    }
  ) { it ->
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
        .padding(it)
    ) {
      LazyColumn {
        // for now we only display posts that have image content.
        // videos are a stretch goal.
        items(models.filter { model -> model.mediaType == ApodMediaType.IMAGE }) { model ->
          ApodCardView(model)
        }
      }
    }
  }
}