package org.cryptotrader.api.library.scripts.http

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * HTTP utility helpers shared by filters and controllers.
 *
 * Scope:
 * - Pure helper functions that derive values (e.g., full request URL) or read immutable inputs (cookies).
 * - No mutation of request/response objects; safe to call from any layer.
 */
object HttpScript {
    /**
     * Build the absolute request URL including the query string, if present.
     *
     * Example:
     * - https://api.example.com/path?x=1
     *
     * @param request the HttpServletRequest to read
     * @return the absolute URL string including query when present
     */
    @JvmStatic
    fun fullUrl(request: HttpServletRequest): String {
        val url: String = request.requestURL.toString()
        val queryString: String? = request.queryString
        return if (queryString.isNullOrBlank()) {
            url
        } else "$url?$queryString"
    }

    /**
     * Read a cookie value by name from the request.
     *
     * @param request the HttpServletRequest containing cookies
     * @param name cookie name to look up
     * @return the cookie value if present; otherwise null
     */
    @JvmStatic
    fun readCookie(request: HttpServletRequest, name: String): String? {
        val cookies: Array<Cookie>? = request.cookies
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
}