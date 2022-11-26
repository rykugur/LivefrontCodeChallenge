package com.example.livefrontcodechallenge.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.utils.errors.DisplayableError
import com.example.livefrontcodechallenge.utils.errors.getDisplayableError
import com.example.livefrontcodechallenge.viewmodel.ErrorState

/**
 * A simple composable that displays the given [errorState] error in a container that expands to
 * fill the available space.
 */
@Composable
fun ErrorView(errorState: ErrorState) {
  val displayableError = remember { errorState.getDisplayableError() }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    Image(
      modifier = Modifier
        .size(75.dp)
        .align(Alignment.CenterHorizontally),
      imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
      colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
      contentDescription = null
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 15.dp))
        .align(Alignment.Start),
      textAlign = TextAlign.Center,
      text = stringResource(id = R.string.error_glitch_in_the_matrix)
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 2.dp))
        .align(Alignment.Start),
      textAlign = TextAlign.Center,
      text = (displayableError as? DisplayableError.DisplayableApiError)?.errorMessage
        ?: stringResource(id = displayableError.message)
    )
    if (displayableError is DisplayableError.DisplayableApiError) {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(PaddingValues(top = 2.dp))
          .align(Alignment.Start),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.error_code_template, displayableError.errorCode)
      )
    }
  }
}