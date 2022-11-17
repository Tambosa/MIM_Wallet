package com.aroman.mimwallet

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

fun Context.getApiKey() = packageManager
    .getApplicationInfo(
        packageName,
        PackageManager.GET_META_DATA
    ).metaData["com.aroman.mimwallet.API_KEY"].toString()