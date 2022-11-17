package com.example.livefrontcodechallenge.di

import com.example.livefrontcodechallenge.BuildConfig
import com.example.livefrontcodechallenge.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
  companion object {
    private const val BASE_URL = "https://api.nasa.gov"
  }

  @Provides
  @Singleton
  fun providesOkHttp(): OkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(apiKeyInterceptor)
    .build()

  @Provides
  @Singleton
  fun providesApodApi(okHttpClient: OkHttpClient): ApodApi =
    buildBaseRetrofit("${BASE_URL}/planetary/apod", okHttpClient).create(ApodApi::class.java)

  private fun buildBaseRetrofit(
    path: String,
    okHttpClient: OkHttpClient
  ): Retrofit = Retrofit.Builder()
    .baseUrl(path)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  /**
   * Intercepts every out-going request and appends the API key to the query parameters.
   */
  private val apiKeyInterceptor: Interceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val originalUrl = originalRequest.url()

    val url = originalUrl.newBuilder()
      .addQueryParameter("apikey", BuildConfig.NASA_API_KEY)
      .build()
    val requestBuilder: Request.Builder = originalRequest.newBuilder()
      .url(url)

    return@Interceptor chain.proceed(requestBuilder.build())
  }
}