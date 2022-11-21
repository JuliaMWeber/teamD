package de.thm.mow2gamecollection.model

interface EmulatorEnabledMultiplayerGame {
        /*
        handle messages sent over the network to indicate the opponent's game moves
         */
        fun handleNetworkMessage(msg: String)
}