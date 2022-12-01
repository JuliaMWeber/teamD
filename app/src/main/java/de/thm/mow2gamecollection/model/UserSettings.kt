package de.thm.mow2gamecollection.model

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
//import androidx.preference.PreferenceManager


// DEBUGGING
private const val DEBUG = true
private const val TAG = "UserSettings"

object UserSettings {
    private const val USER_NAME_KEY = "USER_NAME"
    val IS_FIRST_RUN_KEY = "IS_FIRST_RUN"

    fun saveUserName(context: Context, name: String) {
        Log.d(TAG, "saveUserName: $name")
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString(USER_NAME_KEY, name)
            apply()
        }
    }

    fun getUserName(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_NAME_KEY, null)
    }
}