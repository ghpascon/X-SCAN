package com.smartx.rfidreader.core.webhook

import java.util.Date

data class WebhookSendStatus(
    val timestamp: Date,
    val success: Boolean,
    val sentCount: Int,
    val error: String?
)
