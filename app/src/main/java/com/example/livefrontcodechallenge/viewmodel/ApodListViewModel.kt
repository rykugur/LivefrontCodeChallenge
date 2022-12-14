package com.example.livefrontcodechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import com.example.livefrontcodechallenge.repository.ApodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// alternatively could go with an event type system and emit events, example: emit(Success(List<ApodModel>)).
data class ApodListState(
  val models: List<ApodModel> = listOf(),
  val isLoading: Boolean = false,
  val isInitialLoad: Boolean = false,
  val error: ErrorState? = null
)

@HiltViewModel
class ApodListViewModel @Inject constructor(
  private val apodRepository: ApodRepository
) : ViewModel() {
  private val _stateFlow = MutableStateFlow(ApodListState())
  val stateFlow = _stateFlow.asStateFlow()

  init {
    getApods(isInitialLoad = true)
  }

  fun onPullRefresh() {
    getApods()
  }

  private fun getApods(isInitialLoad: Boolean = false) {
    viewModelScope.launch {
      // copy existing state so as to not throw data on screen
      _stateFlow.emit(_stateFlow.value.copy(isInitialLoad = isInitialLoad, isLoading = true))

      when (val wrapper = apodRepository.getApods()) {
        is ApodResultWrapper.Success -> {
          _stateFlow.emit(ApodListState(models = wrapper.apodModels))
        }
        is ApodResultWrapper.ApiError -> {
          with(wrapper.errorModel) {
            _stateFlow.emit(
              ApodListState(
                isLoading = false,
                error = ErrorState.ApiError(this.msg, this.code)
              )
            )
          }
        }
        is ApodResultWrapper.NetworkError -> {
          _stateFlow.emit(ApodListState(isLoading = false, error = ErrorState.NetworkError))
        }
        is ApodResultWrapper.GenericError -> {
          _stateFlow.emit(ApodListState(isLoading = false, error = ErrorState.GenericError))
        }
      }
    }
  }
}