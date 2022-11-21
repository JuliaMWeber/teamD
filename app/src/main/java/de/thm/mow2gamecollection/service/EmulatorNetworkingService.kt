package de.thm.mow2gamecollection.service

import android.os.Handler
import android.os.Looper
import android.util.Log
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.UnknownHostException
import java.util.concurrent.Executors

// DEBUGGING
private const val DEBUG = true
private const val TAG = "EmulatorNetworkingServ"

class EmulatorNetworkingService(val activity: EmulatorEnabledMultiplayerGame, val isServer: Boolean) {
    private val serverPort = 6000 // port the emulator instances listen to for incoming connections
    private val serverHostPort = 5000 // port on host machine which is forwarded to the serverPort on the emulator
    val clientHostPort = 5003 // port on host machine which is forwarded to the clientPort on the emulator
    private var serverIp: String? = "10.0.2.2"
    private var socket: Socket? = null
    private var serverSocket: ServerSocket? = null
    var updateUiHandler: Handler? = null

    fun start() {
        updateUiHandler = Handler(Looper.getMainLooper())

        // If we're the server start a ServerThread, else connect to the server
        if (isServer) {
            Thread(ServerThread(serverPort)).start()
        } else {
            Thread(ClientThread(serverHostPort)).start()
            // We can't use the same socket for the responses from the "server".
            // Therefore, also start a ServerThread / open a separate socket on the "client" for the "server" to connect to.
            Thread(ServerThread(serverPort)).start()
        }
    }

    fun sendMessage(str: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                if (DEBUG) Log.d(TAG, "$str -> ${socket!!.inetAddress}:${socket!!.port}")
                val out = PrintWriter(socket!!.getOutputStream())
                out.println(str)
                out.flush()
            } catch (e: UnknownHostException) {
                if (DEBUG) Log.e(TAG, e.toString())
                e.printStackTrace()
            } catch (e: IOException) {
                if (DEBUG) Log.e(TAG, e.toString())
                e.printStackTrace()
            } catch (e: Exception) {
                if (DEBUG) Log.e(TAG, e.toString())
                e.printStackTrace()
            }
        }
    }

    fun closeServerSocket() {
        try {
            serverSocket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Opens a socket on the server device to get messages
    inner class ServerThread(private val port: Int) : Thread() {
        override fun run() {
            if (DEBUG) Log.d(TAG, "running ServerThread")
            try {
                // Create a socket on port 6000
                serverSocket = ServerSocket(port)
                if (DEBUG) Log.d(TAG, "serverSocket: ${serverSocket.toString()}")

            } catch (e: IOException) {
                e.printStackTrace()
            }
            while (!currentThread().isInterrupted) {
                try {
                    if (DEBUG) Log.d(TAG, "listening on port $port")
                    // Start listening for messages
                    val mySocket = serverSocket!!.accept()
                    if (DEBUG) Log.d(TAG, "ServerThread: mySocket: $mySocket")
                    val commThread = CommunicationThread(mySocket)
                    Thread(commThread).start()
                    if (isServer) {
                        // Connect to the "server port" of the "client"
                        // We need to send our messages through a different socket.
                        if (DEBUG) Log.d(TAG, "is server -> start ClientThread")
                        Thread(ClientThread(clientHostPort)).start()
                    }
                } catch (e: IOException) {
                    if (DEBUG) Log.d(TAG, e.toString())
                    e.printStackTrace()
                }
            }
        }
    }

    // Handles received messages from clients
    inner class CommunicationThread(clientSocket: Socket?) : Thread() {
        private var input: BufferedReader? = null

        init {
            if (DEBUG) Log.d(TAG, "running CommunicationThread: clientSocket: ${clientSocket.toString()}")
            try {
                // read received data
                input = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun run() {
            if (DEBUG) Log.d(TAG, "running CommunicationThread")
            while (!currentThread().isInterrupted) {
                try {
                    val read = input?.readLine()
                    if (read != null) {
                        updateUiHandler!!.post(UpdateUIThread(read))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Handles connection to server
    inner class ClientThread(private val port: Int) : Thread() {
        override fun run() {
            if (DEBUG) Log.d(TAG, "running ClientThread. socket == null? ${socket == null}")
            while(socket == null) {
                if (DEBUG) Log.d(TAG, "try to connect…")
                try {
                    // connect to server
                    val serverAddress = InetAddress.getByName(serverIp)
                    socket = Socket(serverAddress, port)
                    if (DEBUG) Log.d(TAG, "ClientThread: socket: ${socket!!.inetAddress}:${socket!!.port}")
                } catch (e: UnknownHostException) {
                    if (DEBUG) Log.d(TAG, e.toString())
                    e.printStackTrace()
                } catch (e: IOException) {
                    if (DEBUG) Log.d(TAG, e.toString())
                    e.printStackTrace()
                }
            }
        }
    }

    // Handles showing received messages on screen
    inner class UpdateUIThread(private val msg: String) : Runnable {
        // Print message on screen
        override fun run() {
            activity.handleNetworkMessage(msg)
        }
    }
}