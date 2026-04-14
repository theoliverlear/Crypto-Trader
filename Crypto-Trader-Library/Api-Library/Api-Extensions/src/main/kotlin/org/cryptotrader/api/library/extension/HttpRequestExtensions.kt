package org.cryptotrader.api.library.extension

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * HTTP request extensions for deriving values or reading immutable inputs.
 */

/**
 * Build the absolute request URL including the query string, if present.
 *
 * Example:
 * - https://api.example.com/path?x=1
 *
 * @return the absolute URL string including query when present
 */
fun HttpServletRequest.fullUrl(): String {
    val url: String = this.requestURL.toString()
    val queryString: String? = this.queryString
    return if (queryString.isNullOrBlank()) {
        url
    } else "$url?$queryString"
}

/**
 * Read a cookie value by name from the request.
 *
 * @param name cookie name to look up
 * @return the cookie value if present; otherwise null
 */
fun HttpServletRequest.readCookie(name: String): String? {
    val cookies: Array<Cookie>? = this.cookies
    if (!hasCookies(cookies)) {
        return null
    }
    for (cookie in cookies) {
        if (cookie.name == name) {
            return cookie.value
        }
    }
    return null
}

@OptIn(ExperimentalContracts::class)
private fun hasCookies(cookies: Array<Cookie>?): Boolean {
    contract {
        returns(true) implies (cookies != null)
    }
    return cookies != null && cookies.isNotEmpty()
}