package com.example.livefrontcodechallenge.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.livefrontcodechallenge.data.ApodModel
import com.example.livefrontcodechallenge.utils.MoshiUtils
import com.example.livefrontcodechallenge.utils.dateFormatter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import java.time.LocalDate

@Dao
interface ApodDao {
  companion object {
    const val TABLE_NAME = "apods"
  }

  @Query("SELECT * FROM $TABLE_NAME WHERE date = :date LIMIT 1")
  suspend fun getApod(date: String): ApodModel?

  @Query("SELECT * FROM $TABLE_NAME")
  suspend fun getApods(): List<ApodModel>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(model: ApodModel)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(models: List<ApodModel>)

  @Query("DELETE FROM $TABLE_NAME")
  suspend fun deleteAll()

  @Delete
  suspend fun delete(apod: ApodModel)
}

@Database(entities = [ApodModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class ApodDatabase : RoomDatabase() {
  abstract fun ApodDao(): ApodDao
}

private class Converters {
  private val moshi = MoshiUtils.getMoshiBuilder().build()

  @TypeConverter
  fun fromMap(map: Map<String, String>?): String? {
    if (map == null) {
      return null
    }

    val type = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
    val adapter: JsonAdapter<Map<String, String>> = moshi.adapter(type)
    return adapter.toJson(map)
  }

  @TypeConverter
  fun toMap(map: String?): Map<String, String>? {
    if (map == null) {
      return null
    }

    val type = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
    val adapter: JsonAdapter<Map<String, String>> = moshi.adapter(type)
    return map.let { adapter.fromJson(it) }
  }

  @TypeConverter
  fun fromLocalDate(localDate: LocalDate): String = dateFormatter.format(localDate)

  @TypeConverter
  fun toLocalDate(localDate: String): LocalDate = LocalDate.from(dateFormatter.parse(localDate))
}