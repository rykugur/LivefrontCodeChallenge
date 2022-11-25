package com.example.livefrontcodechallenge.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.livefrontcodechallenge.BuildConfig
import com.example.livefrontcodechallenge.api.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
  companion object {
    private const val BASE_URL = "https://api.nasa.gov/"
  }

  @Provides
  @Singleton
  fun providesOkHttp(@ApplicationContext appContext: Context): OkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(apiKeyInterceptor)
    .addInterceptor(NetworkConnectivityInterceptor(appContext))
    .build()

  @Provides
  @Singleton
  fun providesApodApi(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): ApodApi =
    buildBaseRetrofit(okHttpClient, moshi).create(ApodApi::class.java)

  private fun buildBaseRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
    path: String = BASE_URL
  ): Retrofit = Retrofit.Builder()
    .baseUrl(path)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  /**
   * Intercepts every out-going request and appends the API key to the query parameters.
   */
  private val apiKeyInterceptor: Interceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val originalUrl = originalRequest.url

    val url = originalUrl.newBuilder()
      .addQueryParameter(ApodApi.API_KEY, BuildConfig.NASA_API_KEY)
      .build()
    val requestBuilder: Request.Builder = originalRequest.newBuilder()
      .url(url)

    return@Interceptor chain.proceed(requestBuilder.build())
  }

  private class NetworkConnectivityInterceptor(private val appContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
      if (!isConnected()) {
        throw IOException("no network connectivity")
      }

      return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
      // if we were on API 23 or higher we could use getSystemService(ConnectivityManager::class.java) instead
      val cm: ConnectivityManager = if (android.os.Build.VERSION.SDK_INT >= 23) {
        appContext.getSystemService(ConnectivityManager::class.java)
      } else {
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      }

      return if (android.os.Build.VERSION.SDK_INT >= 23) {
        val network = cm.activeNetwork
        network != null && cm.getNetworkCapabilities(network) != null
      } else {
        val info = cm.activeNetworkInfo
        info != null && info.isConnectedOrConnecting
      }
    }
  }
}