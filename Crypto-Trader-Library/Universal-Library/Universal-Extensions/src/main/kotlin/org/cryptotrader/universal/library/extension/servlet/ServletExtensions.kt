package org.cryptotrader.universal.library.extension.servlet

import jakarta.servlet.http.HttpServletRequest

fun HttpServletRequest.getAuthHeader(): String? = this.getHeader("Authorization")
fun HttpServletRequest.getDpopHeader(): String? = this.getHeader("DPoP")