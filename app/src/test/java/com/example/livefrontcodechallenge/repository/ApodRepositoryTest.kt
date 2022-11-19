package com.example.livefrontcodechallenge.repository

import com.example.livefrontcodechallenge.api.ApodApi
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.jupiter.api.Assertions
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal class ApodRepositoryTest {
  private val mockApi: ApodApi = mockk()

  private val repository: ApodRepository = ApodRepository(mockApi)

  @Test
  fun itReturnsSuccess() {
    coEvery { mockApi.getApods(any()) } returns Response.success(listOf(mockk()))

    val wrapper = runBlocking {
      repository.getApods()
    }

    coVerify { mockApi.getApods(any()) }
    Assertions.assertInstanceOf(ApodResultWrapper.Success::class.java, wrapper)
  }

  @Test
  fun itParsesApiError() {
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"),
      "{\"code\": 400, \"msg\": \"Some interesting error message\", \"service_version\": \"v1\"}"
    )
    coEvery { mockApi.getApods(any()) } throws HttpException(mockk(relaxed = true) {
      every { isSuccessful } returns false
      every { errorBody() } returns responseBody
    })

    val wrapper = runBlocking {
      repository.getApods()
    }

    coVerify { mockApi.getApods(any()) }
    Assertions.assertInstanceOf(ApodResultWrapper.ApiError::class.java, wrapper)
    Assertions.assertEquals(400, (wrapper as ApodResultWrapper.ApiError).errorModel.code)
  }

  @Test
  fun itHandlesHttpException() {
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"),
      "{\"code\": 400, \"msg\": \"Some interesting error message\", \"service_version\": \"v1\"}"
    )
    coEvery { mockApi.getApods(any()) } throws HttpException(mockk(relaxed = true) {
      every { isSuccessful } returns false
      every { errorBody() } returns responseBody
    })

    val wrapper = runBlocking {
      repository.getApods()
    }

    coVerify { mockApi.getApods(any()) }
    Assertions.assertInstanceOf(ApodResultWrapper.ApiError::class.java, wrapper)
    Assertions.assertEquals(400, (wrapper as ApodResultWrapper.ApiError).errorModel.code)
  }

  @Test
  fun itHandlesIOException() {
    coEvery { mockApi.getApods(any()) } throws IOException()

    val wrapper = runBlocking {
      repository.getApods()
    }

    coVerify { mockApi.getApods(any()) }
    Assertions.assertInstanceOf(ApodResultWrapper.NetworkError::class.java, wrapper)
  }
}