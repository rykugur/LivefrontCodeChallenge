package com.example.livefrontcodechallenge.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
  @Provides
  @Singleton
  fun providesMoshi(): Moshi = Moshi.Builder()
    .build()
}