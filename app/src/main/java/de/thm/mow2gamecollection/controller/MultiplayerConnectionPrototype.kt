package de.thm.mow2gamecollection.controller

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.model.NetworkServiceDiscoveryHelper

class MultiplayerConnectionPrototype : AppCompatActivity() {
    private val TAG = "MultiplayerConnectionPrototype"

    private lateinit var nsdHelper: NetworkServiceDiscoveryHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_connection_prototype)

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
        // TODO
    }
}