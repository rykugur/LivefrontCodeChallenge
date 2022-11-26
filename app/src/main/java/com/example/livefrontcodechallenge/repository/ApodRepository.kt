package com.example.livefrontcodechallenge.repository

import com.example.livefrontcodechallenge.api.ApodApi
import com.example.livefrontcodechallenge.data.ApodResultWrapper
import com.example.livefrontcodechallenge.data.ErrorModel
import com.example.livefrontcodechallenge.data.ErrorModelJsonAdapter
import com.example.livefrontcodechallenge.data.db.ApodDao
import com.example.livefrontcodechallenge.utils.MoshiUtils
import com.example.livefrontcodechallenge.utils.dateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import java.time.LocalDate

class ApodRepository(
  private val apodApi: ApodApi,
  private val apodDao: ApodDao
) {

  /**
   * Gets a range of APOD's and returns the wrapped result.
   *
   * @param startDate the startDate of the range, or 30 days before [endDate] if not given.
   * @param endDate the endDate of the range, or today's date if not given.
   */
  // todo: stretch goal: return cached APODs, with some mechanism to forcefully refresh every so often
  suspend fun getApods(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null
  ): ApodResultWrapper {
    val end = endDate ?: LocalDate.now()
    val start = startDate ?: end.minusDays(30)
    val queryParams = mapOf(
      ApodApi.START_DATE to dateFormatter.format(start),
      ApodApi.END_DATE to dateFormatter.format(end),
    )
    return getWrappedResult(queryParams)
  }

  private suspend fun getWrappedResult(queryParams: Map<String, String>): ApodResultWrapper =
    withContext(
      Dispatchers.IO
    ) {
      kotlin.runCatching {
        val response = apodApi.getApods(queryParams)
        when (response.isSuccessful) {
          true -> {
            val body =
              response.body() ?: throw InvalidObjectException("successful response had null body")
            apodDao.insert(body)
            ApodResultWrapper.Success(body)
          }
          false -> {
            response.errorBody()?.let {
              val errorModel = convertErrorBody(it)
              errorModel?.let { ApodResultWrapper.ApiError(errorModel) }
            } ?: ApodResultWrapper.NetworkError
          }
        }
      }.onFailure {
        Timber.d(
          it,
          "ApodRepository.getApod: error getting APOD; thread=${Thread.currentThread().name}"
        )
      }.getOrElse { throwable ->
        when (throwable) {
          is IOException -> ApodResultWrapper.NetworkError
          is HttpException -> convertErrorBody(throwable.response()?.errorBody())?.let {
            ApodResultWrapper.ApiError(it)
          } ?: ApodResultWrapper.GenericError
          else -> {
            Timber.d(throwable, "internal error")
            ApodResultWrapper.GenericError
          }
        }
      }
    }

  /**
   * Attempts to convert an [HttpException] into an [ErrorModel], or returns null
   */
  private fun convertErrorBody(errorBody: ResponseBody?): ErrorModel? {
    return kotlin.runCatching {
      errorBody?.source()?.let {
        ErrorModelJsonAdapter(MoshiUtils.getMoshiBuilder().build()).fromJson(it)
      }
    }.onFailure {
      Timber.d(it, "failed parsing errorBody")
    }.getOrNull()
  }
}