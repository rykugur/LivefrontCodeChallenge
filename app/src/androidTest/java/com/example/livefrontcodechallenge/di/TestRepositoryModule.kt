package com.example.livefrontcodechallenge.di

import com.example.livefrontcodechallenge.api.ApodApi
import com.example.livefrontcodechallenge.data.db.ApodDao
import com.example.livefrontcodechallenge.repository.ApodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [RepositoryModule::class]
)
class TestRepositoryModule {
  @Singleton
  @Provides
  fun providesTestRepositoryModule(apodApi: ApodApi, apodDao: ApodDao): ApodRepository =
    ApodRepository(apodApi, apodDao)
}