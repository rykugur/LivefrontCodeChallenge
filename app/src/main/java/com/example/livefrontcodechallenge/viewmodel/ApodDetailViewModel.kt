package com.example.livefrontcodechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.data.db.ApodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApodDetailState(
  val model: ApodModel? = null,
  val isLoading: Boolean = false,
  val error: ErrorState? = null
)

object ApodNotFound : ErrorState()

@HiltViewModel
class ApodDetailViewModel @Inject constructor(
  val apodDao: ApodDao
) : ViewModel() {
  private val _stateFlow = MutableStateFlow(ApodDetailState())
  val stateFlow = _stateFlow.asStateFlow()

  fun getApod(date: String) {
    viewModelScope.launch {
      val apod = apodDao.getApod(date)
      apod?.let {
        _stateFlow.emit(ApodDetailState(model = it))
      } ?: _stateFlow.emit(ApodDetailState(error = ApodNotFound))
    }
  }
}