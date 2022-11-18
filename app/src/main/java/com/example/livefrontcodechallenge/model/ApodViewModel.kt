package com.example.livefrontcodechallenge.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import com.example.livefrontcodechallenge.repository.ApodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ApodListEvents {
  object Initial : ApodListEvents()
  object Loading : ApodListEvents()
  data class Success(val models: List<ApodModel>) : ApodListEvents()
  data class Failure(val message: String) : ApodListEvents()
  object NetworkError : ApodListEvents()
  object GenericError : ApodListEvents()
}

@HiltViewModel
class ApodListViewModel @Inject constructor(
  private val apodRepository: ApodRepository
) : ViewModel() {
  private val _sharedFlow = MutableSharedFlow<ApodListEvents>()
  val flow = _sharedFlow.asSharedFlow()

  val state = ApodListEvents.Initial

  init {
    viewModelScope.launch {
      _sharedFlow.emit(ApodListEvents.Loading)
      getApods()
    }
  }

  private suspend fun getApods() {
    when (val wrapper = apodRepository.getApods()) {
      is ApodResultWrapper.Success -> {
        _sharedFlow.emit(ApodListEvents.Success(wrapper.apodModels))
      }
      is ApodResultWrapper.ApiError -> {
        _sharedFlow.emit(ApodListEvents.Failure(wrapper.errorModel.msg))
      }
      is ApodResultWrapper.NetworkError -> {
        _sharedFlow.emit(ApodListEvents.NetworkError)
      }
      is ApodResultWrapper.GenericError -> {
        _sharedFlow.emit(ApodListEvents.GenericError)
      }
    }
  }
}