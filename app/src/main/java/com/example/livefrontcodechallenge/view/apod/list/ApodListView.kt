package com.example.livefrontcodechallenge.view.apod.list

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodMediaType
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.view.AppBar
import com.example.livefrontcodechallenge.viewmodel.ApodListViewModel
import com.example.livefrontcodechallenge.viewmodel.ErrorState
import kotlinx.coroutines.launch

@Composable
fun ApodListView(navController: NavController) {

  val viewModel: ApodListViewModel = hiltViewModel()

  // todo: loading indicator
  // todo: pull to refresh
  // todo : could be fun to add ability to pick different list views, e.g. list vs grid
  // or display grid view in landscape mode
  val models = remember { mutableStateListOf<ApodModel>() }
  val loading = remember { mutableStateOf(false) }
  val error = remember { mutableStateOf<ErrorState?>(null) }

  val lifeCycleOwner = LocalLifecycleOwner.current
  LaunchedEffect(key1 = true) {
    launch {
      lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.stateFlow.collect { state ->
          loading.value = state.isLoading

          state.error?.let {
            error.value = it
            return@collect
          }

          models.clear()
          models.addAll(state.models)
        }
      }
    }
  }

  LivefrontCodeChallengeTheme {
    error.value?.let {
      val errorMsg = when (it) {
        // todo: in the case of an ApiError we could do some additional error code checking
        // to display a more user-friendly message
        is ErrorState.ApiError -> stringResource(id = R.string.generic_error)
        ErrorState.GenericError -> stringResource(id = R.string.generic_error)
        ErrorState.NetworkError -> stringResource(id = R.string.network_error)
        else -> null
      }
      errorMsg?.let { msg -> Toast.makeText(LocalContext.current, msg, Toast.LENGTH_SHORT).show() }
      return@LivefrontCodeChallengeTheme
    }

    LivefrontCodeChallengeTheme {
      AppBar(navController) {
        LazyColumn {
          // for now we only display posts that have image content.
          // videos are a stretch goal.
          items(models.filter { model -> model.mediaType == ApodMediaType.IMAGE }) { model ->
            ApodCardView(navController, model)
          }
        }
      }
    }
  }
}