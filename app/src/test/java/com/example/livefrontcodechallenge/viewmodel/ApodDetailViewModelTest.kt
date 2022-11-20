package com.example.livefrontcodechallenge.viewmodel

import com.example.livefrontcodechallenge.CoroutineTestRule
import com.example.livefrontcodechallenge.data.db.ApodDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class ApodDetailViewModelTest {
  @get:Rule
  val coroutineTestRule = CoroutineTestRule()

  private val mockDao: ApodDao = mockk()

  @Test
  fun `it displays cached apod`() = runTest {
    coEvery { mockDao.getApod(any()) } returns mockk()

    val results = mutableListOf<ApodDetailState>()
    val viewModel = ApodDetailViewModel(mockDao)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.getApod("10-10-10")
    delay(100L)

    assertTrue(results.any { it.model != null })

    job.cancel()
  }

  @Test
  fun `it displays apod not found`() = runTest {
    coEvery { mockDao.getApod(any()) } returns null

    val results = mutableListOf<ApodDetailState>()
    val viewModel = ApodDetailViewModel(mockDao)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.getApod("10-10-10")
    delay(100L)

    assertTrue(results.any { it.error is ApodNotFound})

    job.cancel()
  }
}