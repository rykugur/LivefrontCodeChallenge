package com.example.livefrontcodechallenge.view.apod.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.utils.dateFormatter
import com.example.livefrontcodechallenge.view.AppBar
import com.example.livefrontcodechallenge.view.ErrorView
import com.example.livefrontcodechallenge.view.NoContentView
import com.example.livefrontcodechallenge.viewmodel.ApodDetailState
import com.example.livefrontcodechallenge.viewmodel.ApodDetailViewModel
import java.time.LocalDate

@Composable
fun ApodDetailView(
  navController: NavController,
  date: String
) {
  val viewModel: ApodDetailViewModel = hiltViewModel()
  val state: ApodDetailState by viewModel.stateFlow.collectAsState()

  LaunchedEffect(key1 = date) {
    viewModel.getApod(date)
  }

  LivefrontCodeChallengeTheme {
    AppBar(navController) {
      state.error?.let { ErrorView(errorState = it) } ?: ContentView(model = state.model)
    }
  }
}

@Composable
private fun ContentView(model: ApodModel?) {
  if (model == null) {
    NoContentView()
  } else {
    Box(modifier = Modifier.fillMaxSize()) {
      Column {
        AsyncImage(
          model = model.hdUrl ?: model.url, contentDescription = model.title,
          modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
          contentScale = ContentScale.FillWidth
        )
        TextDetails(model = model)
      }
    }
  }
}

@Composable
private fun TextDetails(model: ApodModel) {
  Box(
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .fillMaxSize()
      .padding(PaddingValues(vertical = 6.dp, horizontal = 2.dp))
  ) {
    Column {
      Title(model.title)
      Date(model.date)
      Explanation(model.explanation)
    }
  }
}

@Composable
private fun Title(title: String) {
  Row {
    Text(
      modifier = Modifier.padding(PaddingValues(end = 2.dp)),
      text = stringResource(id = R.string.label_title)
    )
    Text(text = title)
  }
}

@Composable
private fun Date(date: LocalDate) {
  Row {
    Text(
      modifier = Modifier.padding(PaddingValues(end = 2.dp)),
      text = stringResource(id = R.string.label_date)
    )
    Text(text = dateFormatter.format(date))
  }
}

@Composable
fun Explanation(explanation: String) {
  Row {
    Text(modifier = Modifier.padding(PaddingValues(top = 2.dp)), text = explanation)
  }
}