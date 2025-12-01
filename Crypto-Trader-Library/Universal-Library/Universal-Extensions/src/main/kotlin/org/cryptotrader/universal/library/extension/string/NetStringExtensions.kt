package org.cryptotrader.universal.library.extension.string

fun String.isDpop(): Boolean = this.startsWith("DPoP ")
fun String.isBearer(): Boolean = this.startsWith("Bearer ")
fun String.getBearerToken(): String = this.substringAfter("Bearer ").trim()
fun String.getDpopToken(): String = this.substringAfter("DPoP ").trim()