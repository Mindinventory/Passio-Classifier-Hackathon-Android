package com.mindinventory.dermatai.common.utils

import android.Manifest

object Constant {

    const val SPLASH_DEFAULT_TIME = 2000L // 2 seconds
    const val DEFAULT_RESET_SCANNING = -1L
    const val SCANNING_TIME_LIMIT_IN_MILLIS = 5000 // 5 seconds

    val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}