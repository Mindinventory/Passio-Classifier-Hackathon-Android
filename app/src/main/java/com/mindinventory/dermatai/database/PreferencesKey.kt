package com.mindinventory.dermatai.database

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKey {
    val TUTORIAL_KEY: Preferences.Key<Boolean> = booleanPreferencesKey("is_tutorial_watch")
}