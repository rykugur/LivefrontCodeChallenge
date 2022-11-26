package com.example.livefrontcodechallenge.utils.errors

import androidx.annotation.StringRes
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.viewmodel.ApodNotFound
import com.example.livefrontcodechallenge.viewmodel.ErrorState

@StringRes
fun ErrorState.getDisplayableErrorMessage(): Int {
  // in the case of an ApiError we could/should do some additional checking to display
  // a more user-friendly message; e.g. map error code -> displayable error string
  return when (this) {
    is ErrorState.ApiError -> R.string.error_generic
    ErrorState.GenericError -> R.string.error_generic
    ErrorState.NetworkError -> R.string.error_network
    ApodNotFound -> R.string.error_generic
  }
}

fun ErrorState.getDisplayableError(): DisplayableError {
  return when (this) {
    is ErrorState.ApiError -> DisplayableError.DisplayableApiError(
      errorMessage = this.msg,
      errorCode = this.code
    )
    ErrorState.GenericError -> DisplayableError.DisplayableGenericError()
    ErrorState.NetworkError -> DisplayableError.DisplayableNetworkError()
    ApodNotFound -> DisplayableError.ApodNotFound()

  }
}