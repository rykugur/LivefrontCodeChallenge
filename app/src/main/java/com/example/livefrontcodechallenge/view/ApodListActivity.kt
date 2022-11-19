package com.example.livefrontcodechallenge.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import com.example.livefrontcodechallenge.viewmodel.ApodListViewModel
import com.example.livefrontcodechallenge.viewmodel.ErrorState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApodListActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel: ApodListViewModel by viewModels()

    setContent {
      // todo: loading indicator
      // todo: pull to refresh
      // todo: error
      val models = remember { mutableStateListOf<ApodModel>() }
      val loading = remember { mutableStateOf(false) }
      val error = remember { mutableStateOf<ErrorState?>(null) }

      LaunchedEffect(key1 = true) {
        lifecycleScope.launch {
          this@ApodListActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
          }
          Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        }

        // todo : could be fun to add ability to pick different list views, e.g. list vs grid
        ApodListView(models)
      }
    }
  }
}