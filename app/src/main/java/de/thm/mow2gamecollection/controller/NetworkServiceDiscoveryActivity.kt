package de.thm.mow2gamecollection.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.model.NetworkServiceDiscoveryHelper

class NetworkServiceDiscoveryActivity : AppCompatActivity() {
    private val TAG = "NsdMultiplayerP"

    private lateinit var nsdHelper: NetworkServiceDiscoveryHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_service_discovery)

        nsdHelper = NetworkServiceDiscoveryHelper(this)
        nsdHelper.initializeServerSocket()
        nsdHelper.registerService(nsdHelper.mLocalPort)
    }

    override fun onPause() {
        Log.d(TAG, "pause")
        nsdHelper.tearDown()
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG, "resume")
        super.onResume()
        nsdHelper.apply {
            registerService(nsdHelper.mLocalPort)
            discoverServices()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "resume")
        nsdHelper.tearDown()
        super.onDestroy()
    }

    fun onStartDiscoveryButtonClick(view: View) {
        Log.d(TAG, "onStartDiscoveryButtonClick")
//        val serviceInfo = NsdServiceInfo().apply {
//            host = InetAddress.getByName("10.0.2.2")
//            port = 5000
//            serviceType = "_mow2multiplayer._tcp."
//            serviceName = "Mow2MultiplayerXYZ"
//        }
//        nsdHelper.discoveryListener.onServiceFound(serviceInfo)
    }
}