package com.example.livefrontcodechallenge.view.apod.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.livefrontcodechallenge.ui.theme.LivefrontCodeChallengeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class ApodListViewTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

//  @Inject
//  lateinit var viewModel: ApodListViewModel

  @Before
  fun setUp() {
    hiltRule.inject()

    composeTestRule.setContent {
      LivefrontCodeChallengeTheme {
        ApodListView(
          navController = TestNavHostController(ApplicationProvider.getApplicationContext()),
          viewModel = hiltViewModel()
        )
      }
    }
  }

  @Test
  fun verifyListExists() {
    composeTestRule.onNodeWithTag("APOD List")
  }
}