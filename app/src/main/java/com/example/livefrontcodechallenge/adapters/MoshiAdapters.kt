package com.example.livefrontcodechallenge.adapters

import com.example.livefrontcodechallenge.utils.dateFormatter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

class DateAdapter {

  @FromJson
  fun fromJson(date: String): LocalDate = LocalDate.from(dateFormatter.parse(date))

  @ToJson
  fun toJson(date: LocalDate): String = dateFormatter.format(date)
}