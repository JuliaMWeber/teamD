package de.thm.mow2gamecollection.model

import de.thm.mow2gamecollection.controller.FirstRunActivity

class FirstRunModel(val controller: FirstRunActivity) {
    fun saveUserName(name: String) {
        UserSettings.saveUserName(controller, name)
    }
}