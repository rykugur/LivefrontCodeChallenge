package com.example.livefrontcodechallenge.utils

import androidx.annotation.StringRes
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.viewmodel.ApodNotFound
import com.example.livefrontcodechallenge.viewmodel.ErrorState

@StringRes
fun ErrorState.getDisplayableErrorMessage(): Int {
  // in the case of an ApiError we could/should do some additional checking to display
  // a more user-friendly message; e.g. map error code -> displayable error string
  return when (this) {
    is ErrorState.ApiError -> R.string.generic_error
    ErrorState.GenericError -> R.string.generic_error
    ErrorState.NetworkError -> R.string.network_error
    ApodNotFound -> R.string.generic_error
  }

}