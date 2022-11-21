package de.thm.mow2gamecollection.controller

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import de.thm.mow2gamecollection.service.EmulatorNetworkingService

private const val TAG = "ChatActivity"
private const val DEBUG = false

class ChatActivity : AppCompatActivity(), EmulatorEnabledMultiplayerGame {

    private var emulatorNetworkingService: EmulatorNetworkingService? = null



    var receivedText: TextView? = null
    private var yourMessage: EditText? = null
    private var send: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        val isServer = intent.getBooleanExtra("isServer", false)
        emulatorNetworkingService = EmulatorNetworkingService(this, isServer)
        emulatorNetworkingService?.start()

        receivedText = findViewById(R.id.textIncoming)
        yourMessage = findViewById(R.id.textSend)
        send = findViewById(R.id.buttonSend)

        // Send network messages to the other emulator instance
        send!!.setOnClickListener {
            emulatorNetworkingService?.sendMessage(yourMessage!!.text.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        emulatorNetworkingService?.closeServerSocket()
    }

    override fun handleNetworkMessage(msg: String) {
        val text = "${receivedText!!.text} \n$msg"
        if (DEBUG) Log.d(TAG, "received msg: $msg")
        receivedText?.text = text
    }
}