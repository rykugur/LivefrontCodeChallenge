package com.example.livefrontcodechallenge.api

import com.example.livefrontcodechallenge.data.ApodModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Retrofit interface for NASA's Astronomy Picture Of the Day (APOD) API.
 */
interface ApodApi {
  companion object {
    const val DATE_FORMAT = "yyyy-mm-dd"

    const val DATE = "date"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val COUNT = "count"
  }

  @GET("/")
  suspend fun getApod(): Response<ApodModel>

  @GET("/")
  suspend fun getApod(@Query(ApodApi.DATE) date: String): Response<ApodModel>

  @GET("/")
  suspend fun getApods(@QueryMap params: Map<String, String>): Response<List<ApodModel>>
}