package de.thm.mow2gamecollection.controller.helper.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import android.util.Log
import de.thm.mow2gamecollection.controller.WifiDirectActivity

private const val TAG = "WifiDirectBroadcastReceiver"

class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val peerListListener: PeerListListener,
    private val activity: WifiDirectActivity
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Broadcast received!")
        Log.d(TAG, "intent.action: ${intent?.action}")
        when(intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wi-Fi Direct mode is enabled or not, alert the Activity.
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        // Wifi is enabled
                        Log.d(TAG, "wifi enabled")
                    }
                    else -> {
                        // Wifi is disabled
                        Log.d(TAG, "wifi disabled")
                    }
                }

            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
//                // The peer list has changed! We should probably do something about
//                // that.
//                Log.d(TAG, "Peers changed")
//                manager.requestPeers(channel) { peers: WifiP2pDeviceList? ->a
//                    // Handle peers list
//                    Log.d(TAG, peers.toString())
//                }

                // Request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()
                manager.requestPeers(channel, peerListListener)
                Log.d(TAG, "P2P peers changed")
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                // Connection state changed! We should probably do something about
                // that.

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
//                (activity.supportFragmentManager.findFragmentById(R.id.frag_list) as DeviceListFragment)
//                    .apply {
//                        updateThisDevice(
//                            intent.getParcelableExtra(
//                                WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice
//                        )
//                    }
            }
        }
    }
}