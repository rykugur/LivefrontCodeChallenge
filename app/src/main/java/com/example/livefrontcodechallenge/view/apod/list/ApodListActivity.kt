package com.example.livefrontcodechallenge.view.apod.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.livefrontcodechallenge.ui.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApodListActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      Navigation()
    }
  }
}