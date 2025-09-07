@file:JvmName("EmailResponse")
package org.cryptotrader.contact.comm.email.response

data class EmailResponse(
    val subject: String,
    val body: String,
    val to: String)