package com.example.livefrontcodechallenge.data

import java.util.*

/**
 * DTO representing an response from the /apod endpoint.
 */
data class ApodModel(
  val resource: Map<String, String>,
  val title: String,
  val date: Date,
  val url: String,
  val hdurl: String?,
  val media_type: ApodMediaType,
  val explanation: String,
  val thumbnail_url: String?,
  val copyright: String,
  val service_version: String
)

enum class ApodMediaType(val type: String) {
  IMAGE("image"), VIDEO("video")
}