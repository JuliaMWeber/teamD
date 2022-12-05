package de.thm.mow2gamecollection.controller.helper.storage

import android.content.Context
import androidx.preference.PreferenceManager

object UserSettings {
    private const val USER_NAME_KEY = "USER_NAME"
    val IS_FIRST_RUN_KEY = "IS_FIRST_RUN"

    fun saveUserName(context: Context, name: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString(USER_NAME_KEY, name)
            apply()
        }
    }

    fun getUserName(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_NAME_KEY, null)
    }
}