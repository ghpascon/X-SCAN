package com.example.x_scan.rfid

data class EpcTag(
    var id: String = "",
    var epc: String = "",
    var count: String = "1",
    var rssi: String = "",
)
