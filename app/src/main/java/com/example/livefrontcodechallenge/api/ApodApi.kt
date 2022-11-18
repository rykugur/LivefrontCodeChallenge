package com.example.livefrontcodechallenge.api

import com.example.livefrontcodechallenge.data.ApodModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Retrofit interface for NASA's Astronomy Picture Of the Day (APOD) API.
 */
interface ApodApi {
  companion object {
    const val DATE_FORMAT = "uuuu-MM-dd"

    const val API_KEY = "api_key"
    const val DATE = "date"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val COUNT = "count"
  }

  @GET("planetary/apod")
  suspend fun getApod(): Response<ApodModel>

  @GET("planetary/apod")
  suspend fun getApods(@QueryMap params: Map<String, String>): Response<List<ApodModel>>
}