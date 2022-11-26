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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.livefrontcodechallenge.R

@Composable
fun NoContentView() {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    Image(
      modifier = Modifier
        .padding(PaddingValues(top = 10.dp))
        .size(75.dp)
        .align(CenterHorizontally),
      imageVector = ImageVector.vectorResource(id = R.drawable.ic_not_found),
      colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
      contentDescription = null
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingValues(top = 15.dp))
        .align(Start),
      textAlign = TextAlign.Center,
      text = stringResource(id = R.string.no_data)
    )
  }
}