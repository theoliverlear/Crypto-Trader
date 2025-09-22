package org.cryptotrader.api.library.infrastructure.extension

import jakarta.servlet.http.HttpServletRequest

/**
 * Infrastructure extension helpers used across HTTP filters and controllers.
 *
 * These tiny helpers keep intent readable:
 * - String.isDpop(): check whether an Authorization header uses the DPoP scheme.
 * - HttpServletRequest.getAuthHeader(): fetch the raw Authorization header if present.
 */

/**
 * Determine whether this Authorization header value is using the DPoP scheme.
 *
 * Usage example:
 * val auth = request.getHeader("Authorization")
 * if (auth?.isDpop() == true) { /* handle DPoP-bound token */ }
 *
 * @receiver The Authorization header value (e.g., "DPoP eyJ..." or "Bearer eyJ...")
 * @return true if the string starts with "DPoP ", false otherwise.
 */
fun String.isDpop(): Boolean = this.startsWith("DPoP ")

fun String.isBearer(): Boolean = this.startsWith("Bearer ")

fun String.getBearerToken(): String = this.substringAfter("Bearer ").trim()
fun String.getDpopToken(): String = this.substringAfter("DPoP ").trim()
/**
 * Convenience accessor for the Authorization header of an HTTP request.
 *
 * @receiver The HttpServletRequest being inspected.
 * @return The raw Authorization header value, or null if the header is absent.
 */
fun HttpServletRequest.getAuthHeader(): String? = this.getHeader("Authorization")
fun HttpServletRequest.getDpopHeader(): String? = this.getHeader("DPoP")