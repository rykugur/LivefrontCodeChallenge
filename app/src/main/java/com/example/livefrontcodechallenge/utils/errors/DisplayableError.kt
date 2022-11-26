package com.example.livefrontcodechallenge.utils.errors

import androidx.annotation.StringRes
import com.example.livefrontcodechallenge.R

sealed class DisplayableError {
  /**
   * Over-rideable title value. Will display a random error title if no over-ride is provided.
   */
  abstract val message: Int

  data class DisplayableApiError(
    @StringRes override val message: Int = R.string.error_glitch_in_the_matrix,
    val errorMessage: String,
    val errorCode: Int
  ) : DisplayableError()

  data class DisplayableGenericError(
    @StringRes override val message: Int = R.string.error_generic
  ) : DisplayableError()

  data class DisplayableNetworkError(
    @StringRes override val message: Int = R.string.error_network
  ) : DisplayableError()

  data class ApodNotFound(
    @StringRes override val message: Int = R.string.error_not_found
  ) : DisplayableError()
}
