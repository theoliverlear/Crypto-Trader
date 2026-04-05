package org.cryptotrader.agent.library.communication.response

data class FileContentResponse(
    val path: String,
    val totalLines: Int,
    val encoding: String = "UTF-8",
    val content: String,
    val isTruncated: Boolean
)
