package de.thm.mow2gamecollection.controller.helper.connection

import android.app.Activity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import de.thm.mow2gamecollection.model.MultiplayerGame

private const val PACKAGE_NAME = "de.thm.mow2teamd.multiplayerprototype"

class NearbyConnectionManager(activity: MultiplayerGame, payloadCallback: PayloadCallback) {

    private var opponentEndpointId: String? = null


    /**
     * Strategy for telling the Nearby Connections API how we want to discover and connect to
     * other nearby devices. A star shaped strategy means we want to discover multiple devices but
     * only connect to and communicate with one at a time.
     */
    private val strategy = Strategy.P2P_STAR

    /**
     * Our handle to the [Nearby Connections API][ConnectionsClient].
     */
    private var connectionsClient: ConnectionsClient

    init {
        connectionsClient = Nearby.getConnectionsClient(activity as Activity)
    }

    private fun startAdvertising(playerName: String) {
        val options = AdvertisingOptions.Builder().setStrategy(strategy).build()
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
            playerName,
            PACKAGE_NAME,
            connectionLifecycleCallback,
            options
        )
    }

    private fun startDiscovery(){
        val options = DiscoveryOptions.Builder().setStrategy(strategy).build()
        connectionsClient.startDiscovery(PACKAGE_NAME,endpointDiscoveryCallback,options)
    }

    fun findOpponent(playerName: String) {
        startAdvertising(playerName)
        startDiscovery()
    }

    fun disconnect() {
        opponentEndpointId?.let { connectionsClient.disconnectFromEndpoint(it) }
    }

    fun stop() {
        opponentEndpointId = null
        connectionsClient.apply {
            stopAdvertising()
            stopDiscovery()
            stopAllEndpoints()
        }
    }

    fun sendGameMove(payload: ByteArray) {
        connectionsClient.sendPayload(
            opponentEndpointId!!,
            Payload.fromBytes(payload)
        )
    }

    // Callback for connections to other devices
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            // Accepting a connection means you want to receive messages. Hence, the API expects
            // that you attach a PayloadCall to the acceptance
            connectionsClient.acceptConnection(endpointId, payloadCallback)
            activity.opponentName = "Opponent\n(${info.endpointName})"
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                connectionsClient.stopAdvertising()
                connectionsClient.stopDiscovery()
                opponentEndpointId = endpointId
                activity.onSuccessfulConnection()
            }
        }

        override fun onDisconnected(endpointId: String) {
            activity.resetGame()
        }
    }

    // Callback for finding other devices
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            connectionsClient.requestConnection(activity.myName, endpointId, connectionLifecycleCallback)
        }

        override fun onEndpointLost(endpointId: String) {
        }
    }
}