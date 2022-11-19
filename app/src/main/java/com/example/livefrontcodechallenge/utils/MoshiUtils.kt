package com.example.livefrontcodechallenge.utils

import com.example.livefrontcodechallenge.adapters.DateAdapter
import com.example.livefrontcodechallenge.data.ApodMediaTypeAdapters
import com.squareup.moshi.Moshi

class MoshiUtils {
  companion object {
    fun getMoshiBuilder(): Moshi.Builder {
      return Moshi.Builder()
        .add(DateAdapter())
        .add(ApodMediaTypeAdapters())
    }
  }
}