package de.thm.mow2gamecollection.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.helper.connection.WiFiDirectBroadcastReceiver

private const val TAG = "WifiDirectActivity"

class WifiDirectActivity : AppCompatActivity() {
    var isWifiP2pEnabled = false
    private val intentFilter = IntentFilter()
    private val peers = mutableListOf<WifiP2pDevice>()

    private var channel: WifiP2pManager.Channel? = null
    private var receiver: BroadcastReceiver? = null
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        Log.d(TAG, "peerListListener")
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers)

            Log.d(TAG, peers.toString())
        }

        if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return@PeerListListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_direct)

        intentFilter.apply {
            // Indicates a change in the Wi-Fi Direct status.
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)

            // Indicates a change in the list of available peers.
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)

            // Indicates the state of Wi-Fi Direct connectivity has changed.
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)

            // Indicates this device's details have changed.
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        // get an instance of the WifiP2pManager, and call its initialize() method.
        // This method returns a WifiP2pManager.Channel object, which will later be used to connect
        // the app to the Wi-Fi Direct framework.
        channel = manager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager!!, channel, peerListListener, this)

        }
    }

    /* register the broadcast receiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }

    }

    /* unregister the broadcast receiver */
    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }

    fun discoverPeers(view: View) {
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                Log.d(TAG, "discoverPeers: discovery process succeeded")
            }

            override fun onFailure(reasonCode: Int) {
                Log.d(TAG, "discoverPeers: onFailure")
            }
        })
    }
}