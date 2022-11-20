package com.example.livefrontcodechallenge.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.livefrontcodechallenge.data.db.ApodDao.Companion.TABLE_NAME
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.time.LocalDate

/**
 * DTO representing an response from the /apod endpoint.
 *
 * Also used as an Entity for our room DB.
 */
@Entity(tableName = TABLE_NAME)
@JsonClass(generateAdapter = true)
data class ApodModel(
  val resource: Map<String, String>?,
  val title: String,
  @PrimaryKey val date: LocalDate,
  val url: String,
  @field:Json(name = "hdurl") val hdUrl: String?,
  @field:Json(name = "media_type") val mediaType: ApodMediaType = ApodMediaType.UNKNOWN,
  val explanation: String,
  @field:Json(name = "thumbnail_url") val thumbnailUrl: String?,
  val copyright: String?,
  @field:Json(name = "service_version") val serviceVersion: String
)

enum class ApodMediaType(val type: String) {
  IMAGE("image"), VIDEO("video"), UNKNOWN("unknown")
}

@JsonClass(generateAdapter = true)
data class ErrorModel(
  val code: Int,
  val msg: String,
  @field:Json(name = "service_version") val serviceVersion: String
)

sealed class ApodResultWrapper {
  data class Success(val apodModels: List<ApodModel>) : ApodResultWrapper()
  data class ApiError(val errorModel: ErrorModel) : ApodResultWrapper()
  object NetworkError : ApodResultWrapper()
  object GenericError : ApodResultWrapper()
}

class ApodMediaTypeAdapters {
  @FromJson
  fun apod(type: String): ApodMediaType = ApodMediaType.values().firstOrNull {
    it.type == type
  } ?: ApodMediaType.UNKNOWN

  @ToJson
  fun toJson(type: ApodMediaType): String = type.type
}