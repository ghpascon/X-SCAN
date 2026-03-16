package com.example.x_scan.rfid

interface UhfListener {
    fun onRead(tagsJson: String)
    fun onConnect(isConnected: Boolean, powerLevel: Int)
}
