package com.example.livefrontcodechallenge.utils

import com.example.livefrontcodechallenge.api.ApodApi
import java.time.format.DateTimeFormatter
import java.util.*

val dateFormatter: DateTimeFormatter
  get() = DateTimeFormatter.ofPattern(ApodApi.DATE_FORMAT, Locale.getDefault())