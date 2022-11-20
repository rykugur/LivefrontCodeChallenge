package com.example.livefrontcodechallenge.view.apod.list

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.livefrontcodechallenge.data.ApodMediaType
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.utils.getDisplayableErrorMessage
import com.example.livefrontcodechallenge.view.AppBar
import com.example.livefrontcodechallenge.viewmodel.ApodListViewModel
import com.example.livefrontcodechallenge.viewmodel.ErrorState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ApodListView(navController: NavController) {

  val viewModel: ApodListViewModel = hiltViewModel()

  // todo: could be fun to add ability to pick different list views, e.g. list vs grid
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
      Toast.makeText(
        LocalContext.current,
        stringResource(id = it.getDisplayableErrorMessage()),
        Toast.LENGTH_SHORT
      ).show()
      return@LivefrontCodeChallengeTheme
    }

    AppBar(navController) {
      val pullRefreshState = rememberPullRefreshState(refreshing = loading.value, onRefresh = {
        viewModel.onSwipeRefresh()
      })
      Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
      ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
          // for now we only display posts that have image content.
          // videos are a stretch goal.
          items(models.filter { model -> model.mediaType == ApodMediaType.IMAGE }) { model ->
            ApodCardView(navController, model)
          }
        }
        PullRefreshIndicator(
          refreshing = loading.value,
          state = pullRefreshState,
          modifier = Modifier.align(Alignment.TopCenter)
        )
      }
    }
  }
}