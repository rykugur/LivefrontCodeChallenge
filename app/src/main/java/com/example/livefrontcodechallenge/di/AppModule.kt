package com.example.livefrontcodechallenge.di

import android.content.Context
import androidx.room.Room
import com.example.livefrontcodechallenge.data.db.ApodDao
import com.example.livefrontcodechallenge.data.db.ApodDatabase
import com.example.livefrontcodechallenge.utils.MoshiUtils
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
  @Provides
  @Singleton
  fun providesMoshi(): Moshi = MoshiUtils.getMoshiBuilder().build()

  @Provides
  @Singleton
  fun providesApodDatabase(@ApplicationContext context: Context): ApodDatabase =
    Room.databaseBuilder(
      context,
      ApodDatabase::class.java,
      "apods_db"
    ).build()

  @Provides
  @Singleton
  fun providesApodDao(apodDatabase: ApodDatabase): ApodDao = apodDatabase.ApodDao()
}