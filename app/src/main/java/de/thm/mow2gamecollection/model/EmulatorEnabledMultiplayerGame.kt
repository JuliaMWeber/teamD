package de.thm.mow2gamecollection.model

import android.app.Activity
import de.thm.mow2gamecollection.service.EmulatorNetworkingService

interface EmulatorEnabledMultiplayerGame {
        var emulatorNetworkingService: EmulatorNetworkingService?

        fun initEmulatorNetworkingService(activity: Activity, isServer: Boolean) {
                emulatorNetworkingService = EmulatorNetworkingService(this, isServer)
                emulatorNetworkingService?.start()
        }

        // handle messages sent over the network indicating the opponent's game moves
        fun handleNetworkMessage(msg: String)

        fun sendNetworkMessage(msg: String) {
                emulatorNetworkingService?.sendMessage(msg)
        }
}