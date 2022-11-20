package com.example.livefrontcodechallenge.di

import com.example.livefrontcodechallenge.api.ApodApi
import com.example.livefrontcodechallenge.data.db.ApodDao
import com.example.livefrontcodechallenge.repository.ApodRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
  @Provides
  @Singleton
  fun providesApodRepository(
    apodApi: ApodApi,
    apodDao: ApodDao
  ): ApodRepository = ApodRepository(apodApi, apodDao)
}