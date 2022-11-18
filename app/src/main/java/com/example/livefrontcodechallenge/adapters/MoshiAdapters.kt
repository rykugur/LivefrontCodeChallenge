package com.example.livefrontcodechallenge.adapters

import com.example.livefrontcodechallenge.api.ApodApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DateAdapter {
  private val dtFormatter = DateTimeFormatter.ofPattern(ApodApi.DATE_FORMAT, Locale.getDefault())

  @FromJson
  fun fromJson(date: String): LocalDate = LocalDate.from(dtFormatter.parse(date))

  @ToJson
  fun toJson(date: LocalDate): String = dtFormatter.format(date)
}