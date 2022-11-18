package com.example.livefrontcodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.livefrontcodechallenge.repository.ApodRepository
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApodListActivity : ComponentActivity() {

  @Inject
  lateinit var apodRepository: ApodRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      LivefrontCodeChallengeTheme {
        Scaffold(
          topBar = {
            TopAppBar(
              title = { Text(getString(R.string.app_name)) },
              backgroundColor = Color.Black,
              contentColor = Color.White
            )
          },
          content = {
            // A surface container using the 'background' color from the theme
            Surface(
              modifier = Modifier
                .fillMaxSize()
                .padding(it),
              color = MaterialTheme.colors.background
            ) {
              Greeting("Android")
            }
          },
        )
      }
    }
  }
}

@Composable
fun ApodListView() {
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  LivefrontCodeChallengeTheme {
    Greeting("Android")
  }
}