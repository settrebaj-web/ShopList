package com.example.shop

import android.util.Log
import timber.log.Timber

/** Release-те тек WARN және ERROR деңгейлері өтеді. */
class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.WARN) return
        // Мұнда Crashlytics-ке жіберуге болады
    }
}
