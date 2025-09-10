package org.cryptotrader.contact.library.events

data class EmailSentEvent(
    val subject: String,
    val body: String,
    val toAddress: String,
    val fromAddress: String)
