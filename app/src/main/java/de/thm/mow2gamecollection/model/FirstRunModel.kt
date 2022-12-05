package de.thm.mow2gamecollection.model

import android.content.Context
import de.thm.mow2gamecollection.controller.helper.storage.UserSettings

class FirstRunModel(val context: Context) {
    fun saveUserName(name: String) {
        UserSettings.saveUserName(context, name)
    }
}