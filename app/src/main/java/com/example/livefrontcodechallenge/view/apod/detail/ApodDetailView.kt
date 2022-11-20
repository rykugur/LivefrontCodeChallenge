package com.example.livefrontcodechallenge.view.apod.detail

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.utils.dateFormatter
import com.example.livefrontcodechallenge.utils.getDisplayableErrorMessage
import com.example.livefrontcodechallenge.view.AppBar
import com.example.livefrontcodechallenge.viewmodel.ApodDetailState
import com.example.livefrontcodechallenge.viewmodel.ApodDetailViewModel
import com.example.livefrontcodechallenge.viewmodel.ErrorState
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
      state.error?.let {
        Toast.makeText(
          LocalContext.current,
          stringResource(id = it.getDisplayableErrorMessage()),
          Toast.LENGTH_SHORT
        ).show()
        return@AppBar
      }
      state.model?.let {
        Box(modifier = Modifier.fillMaxSize()) {
          Column {
            AsyncImage(
              model = it.hdUrl ?: it.url, contentDescription = it.title,
              modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
              contentScale = ContentScale.FillWidth
            )
            TextDetails(model = it)
          }
        }
      }
    }
  }
}

@Composable
fun TextDetails(model: ApodModel) {
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
fun Title(title: String) {
  Row {
    Text(
      modifier = Modifier.padding(PaddingValues(end = 2.dp)),
      text = stringResource(id = R.string.label_title)
    )
    Text(text = title)
  }
}

@Composable
fun Date(date: LocalDate) {
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