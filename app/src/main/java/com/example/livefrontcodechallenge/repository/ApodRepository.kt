package com.example.livefrontcodechallenge.repository

import com.example.livefrontcodechallenge.api.ApodApi
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import com.example.livefrontcodechallenge.data.ErrorModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApodRepository(
  private val apodApi: ApodApi,
  private val moshi: Moshi
) {
  private val dtFormatter = DateTimeFormatter.ofPattern(ApodApi.DATE_FORMAT)

  /**
   * Gets the APOD for today's date (no parameters passed).
   */
  suspend fun getApods(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null
  ): ApodResultWrapper {
    val end = endDate ?: LocalDate.now()
    val start = startDate ?: end.minusDays(30)
    val queryParams = mapOf(
      ApodApi.START_DATE to dtFormatter.format(start),
      ApodApi.END_DATE to dtFormatter.format(end)
    )
    val response = apodApi.getApods(queryParams)
    return getWrappedResult(response)
  }

  private suspend fun getWrappedResult(response: Response<List<ApodModel>>): ApodResultWrapper =
    withContext(
      Dispatchers.IO
    ) {
      kotlin.runCatching {
        if (!response.isSuccessful) {
          val error = response.errorBody()?.let {
            moshi.adapter(ErrorModel::class.java).fromJson(it.source())
          } ?: ApodResultWrapper.NetworkError
        }

        val body =
          response.body() ?: throw InvalidObjectException("successful response had null body")
        ApodResultWrapper.Success(body)
      }.onFailure {
        Timber.d(
          it,
          "ApodRepository.getApod: error getting APOD; thread=${Thread.currentThread().name}"
        )
      }.getOrElse { throwable ->
        when (throwable) {
          is IOException -> ApodResultWrapper.NetworkError
          is HttpException -> convertErrorBody(throwable)?.let {
            ApodResultWrapper.ApiError(it)
          } ?: ApodResultWrapper.GenericError
          else -> ApodResultWrapper.GenericError
        }
      }
    }

  /**
   * Attempts to convert an [HttpException] into an [ErrorModel], or returns null
   */
  private fun convertErrorBody(throwable: HttpException): ErrorModel? {
    return kotlin.runCatching {
      throwable.response()?.errorBody()?.source()?.let {
        val adapter = moshi.adapter(ErrorModel::class.java)
        adapter.fromJson(it)
      }
    }.getOrNull()
  }
}