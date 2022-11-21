package de.thm.mow2gamecollection.model

import com.google.android.gms.nearby.connection.PayloadCallback

interface MultiplayerGame {
    var nearbyConnectionManager: NearbyConnectionManager
    var opponentName: String?
    var myCodeName: String
    var payloadCallback: PayloadCallback

    /*
    Function called when a connection has successfully been established
     */
    fun onSuccessfulConnection()

    fun resetGame()
}