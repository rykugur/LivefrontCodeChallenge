package com.example.livefrontcodechallenge.viewmodel

import com.example.livefrontcodechallenge.CoroutineTestRule
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import com.example.livefrontcodechallenge.data.ErrorModel
import com.example.livefrontcodechallenge.repository.ApodRepository
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
internal class ApodListViewModelTest {
  @get:Rule
  val coroutineTestRule = CoroutineTestRule()

  private val mockRepository: ApodRepository = mockk()

  @Test
  fun `it displays loading`() = runTest {
    coEvery { mockRepository.getApods() } returns ApodResultWrapper.Success(listOf(mockk()))

    val results = mutableListOf<ApodListState>()
    val viewModel = ApodListViewModel(mockRepository)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.refreshApods()
    delay(100L)

    assertTrue(results.any { it.isLoading })

    job.cancel()
  }

  @Test
  fun `it displays results`() = runTest {
    coEvery { mockRepository.getApods() } returns ApodResultWrapper.Success(listOf(mockk()))

    val results = mutableListOf<ApodListState>()
    val viewModel = ApodListViewModel(mockRepository)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.refreshApods()
    delay(100L)

    assertTrue(results.any { it.models.isNotEmpty() })

    job.cancel()
  }

  @Test
  fun `it displays ApiError`() = runTest {
    coEvery { mockRepository.getApods() } returns ApodResultWrapper.ApiError(ErrorModel(400, "msg", "v1"))

    val results = mutableListOf<ApodListState>()
    val viewModel = ApodListViewModel(mockRepository)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.refreshApods()
    delay(100L)

    assertTrue(results.any { it.error is ErrorState.ApiError })

    job.cancel()
  }

  @Test
  fun `it displays NetworkError`() = runTest {
    coEvery { mockRepository.getApods() } returns ApodResultWrapper.NetworkError

    val results = mutableListOf<ApodListState>()
    val viewModel = ApodListViewModel(mockRepository)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.refreshApods()
    delay(100L)

    assertTrue(results.any { it.error is ErrorState.NetworkError })

    job.cancel()
  }

  @Test
  fun `it displays GenericError`() = runTest {
    coEvery { mockRepository.getApods() } returns ApodResultWrapper.GenericError

    val results = mutableListOf<ApodListState>()
    val viewModel = ApodListViewModel(mockRepository)
    val job = viewModel.stateFlow
      .onEach(results::add)
      .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

    viewModel.refreshApods()
    delay(100L)

    assertTrue(results.any { it.error is ErrorState.GenericError })

    job.cancel()
  }
}