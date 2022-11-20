package com.example.livefrontcodechallenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ChallengeApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    // plant other trees here as needed. Examples: a crashlytics tree to report crashes, an analytics
    // tree to send analytics events via timber
  }
}