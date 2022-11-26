package com.example.livefrontcodechallenge.view.apod.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.livefrontcodechallenge.data.ApodMediaType
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.view.AppBar
import com.example.livefrontcodechallenge.view.ErrorView
import com.example.livefrontcodechallenge.view.NoContentView
import com.example.livefrontcodechallenge.viewmodel.ApodListState
import com.example.livefrontcodechallenge.viewmodel.ApodListViewModel
import com.example.livefrontcodechallenge.viewmodel.ErrorState
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ApodListView(navController: NavController) {

  // todo: could be a fun to add ability to pick different list views, e.g. list vs grid
  // todo: alternatively (or at least) display grid view in landscape mode

  // todo: figure out why refresh indicator sometimes gets stuck on screen when device doesn't
  // have network

  val viewModel: ApodListViewModel = hiltViewModel()
  val state: ApodListState by viewModel.stateFlow.collectAsState()

  val showError = !state.isLoading && state.error != null
  val showNoContent = !state.isLoading && !showError && state.models.isEmpty()

  Timber.tag("DERP").d(".ApodListView: isLoading=${state.isLoading}; thread=${Thread.currentThread().name}")

  LivefrontCodeChallengeTheme {
    AppBar(navController) {
      val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { viewModel.onPullRefresh() }
      )
      Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
      ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
          if (showNoContent) {
            item { NoContentView() }
          } else if (state.error != null) {
            item { ErrorView(errorState = state.error ?: ErrorState.GenericError) }
          } else {
            // for now we only display posts that have image content.
            // videos are a stretch goal.
            items(state.models.filter { model -> model.mediaType == ApodMediaType.IMAGE }) { model ->
              ApodCardView(navController, model)
            }
          }
        }
        PullRefreshIndicator(
          refreshing = state.isLoading,
          state = pullRefreshState,
          modifier = Modifier.align(Alignment.TopCenter)
        )
      }
    }
  }
}