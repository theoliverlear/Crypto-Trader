package org.cryptotrader.api.library.scripts.dpop

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import java.net.URI
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64
import java.util.Locale
import kotlin.math.abs
import kotlin.text.iterator
import kotlin.text.StringBuilder

/**
 * DPoP helper utilities kept dependency-light for reuse across modules.
 *
 * What this is:
 * - Minimal helpers for RFC 9449 Demonstrating Proof-of-Possession at the Application Layer ("DPoP").
 * - Provides RFC 7638 JWK thumbprint computation ("jkt") and basic proof structure validation.
 *
 * What this is NOT:
 * - A cryptographic verifier of DPoP proofs. It does NOT validate the JWS signature. Use the
 *   stronger service-layer verifier (e.g., DpopVerifierService backed by JOSE/JCA) for security decisions.
 *
 * Typical usage:
 * - Read a DPoP proof from the DPoP header, compute its public key thumbprint (jkt), and/or perform
 *   lightweight checks during development.
 *
 * Security note:
 * - Never rely on validateProofBasics for production authorization decisions. It only checks claim shapes
 *   and basic freshness; it cannot detect forged proofs without signature verification.
 */
object DpopScript {

    /** Extract the RFC7638 thumbprint (jkt) from a DPoP proof's JWK header. */
    fun extractJktFromDpopProof(dpopJwt: String?): String? {
        if (dpopJwt.isNullOrBlank()) {
            return null
        }
        val decoded: DecodedJWT = JWT.decode(dpopJwt)
        val jwkMap: Map<String, Any> = decoded.getHeaderClaim("jwk").asMap() ?: return null
        return this.computeJwkThumbprint(jwkMap)
    }

    /**
     * Compute the RFC 7638 JWK thumbprint ("jkt") for RSA or EC public keys.
     *
     * Canonicalization rules:
     * - EC: use members {"crv","kty","x","y"} in lexicographic order with string values.
     * - RSA: use members {"e","kty","n"} in lexicographic order with string values.
     * - Then UTF‑8 encode the canonical JSON and compute base64url(SHA‑256(json)).
     *
     * @param jsonWebKey a decoded JWK map from the DPoP header (must contain required members for RSA or EC)
     * @return base64url SHA‑256 thumbprint, or null when unsupported/malformed
     */
    fun computeJwkThumbprint(jsonWebKey: Map<String, Any>): String? {
        val keyType = jsonWebKey["kty"]?.toString() ?: return null
        val canonicalJson = when (keyType.uppercase(Locale.ROOT)) {
            "RSA" -> this.canonicalizeJson(mapOf(
                "e" to (jsonWebKey["e"]?.toString() ?: return null),
                "kty" to "RSA",
                "n" to (jsonWebKey["n"]?.toString() ?: return null)
            ))
            "EC" -> this.canonicalizeJson(mapOf(
                "crv" to (jsonWebKey["crv"]?.toString() ?: return null),
                "kty" to "EC",
                "x" to (jsonWebKey["x"]?.toString() ?: return null),
                "y" to (jsonWebKey["y"]?.toString() ?: return null)
            ))
            else -> return null
        }
        val sha256: MessageDigest = MessageDigest.getInstance("SHA-256")
        val jsonBytes: ByteArray = canonicalJson.toByteArray(StandardCharsets.UTF_8)
        val digest: ByteArray = sha256.digest(jsonBytes)
        return base64UrlEncode(digest)
    }

    /**
     * Perform basic structural validation of a DPoP proof without cryptographic checks.
     *
     * This method checks only:
     * - htm equals the provided HTTP method
     * - htu, after normalization (scheme/host lowercased, default port removed), equals the provided URL
     * - iat is within the allowed clock skew window
     *
     * It does NOT verify the JWS signature or the optional 'ath' claim. Use a proper verifier in production.
     *
     * @param dpopJwt the compact DPoP JWT from the DPoP header
     * @param requestMethod the HTTP method string to compare against the proof's htm
     * @param requestUri the absolute URL to compare against the proof's htu
     * @param skewToleranceSeconds allowed skew (seconds) for iat freshness check (default 10s)
     * @return true if the structural checks pass; false otherwise
     */
    fun validateProofBasics(dpopJwt: String?,
                            requestMethod: String,
                            requestUri: String,
                            skewToleranceSeconds: Long = 10): Boolean {
        if (dpopJwt.isNullOrBlank()) {
            return false
        }
        val decoded: DecodedJWT = JWT.decode(dpopJwt)
        val method: String = decoded.getClaim("htm").asString() ?: return false
        val httpTokenUri: String = decoded.getClaim("htu").asString() ?: return false
        val issuedAt: Long = decoded.getClaim("iat").asLong() ?: return false
        if (method != requestMethod) {
            return false
        }
        val normalizedHtu: String = this.normalizeUri(httpTokenUri)
        val normalizedRequest: String = this.normalizeUri(requestUri)
        if (normalizedHtu != normalizedRequest) {
            return false
        }

        val now: Long = Instant.now().epochSecond
        val deltaSeconds: Long = abs(now - issuedAt)
        return deltaSeconds <= skewToleranceSeconds
    }

    private fun normalizeUri(baseUri: String): String {
        val uri = URI(baseUri)
        val scheme: String? = uri.scheme?.lowercase(Locale.ROOT)
        val host: String? = uri.host?.lowercase(Locale.ROOT)
        val portPart: String = if (this.isPortSpecified(uri, scheme)) {
            ":${uri.port}"
        } else ""
        val path: String = uri.path ?: "/"
        val query: String = if (!uri.query.isNullOrBlank()) "?${uri.query}" else ""
        return "$scheme://$host$portPart$path$query"
    }

    private fun isPortSpecified(uri: URI, scheme: String?): Boolean =
        uri.port != -1 && uri.port != defaultPort(scheme)

    private fun defaultPort(scheme: String?): Int = when (scheme?.lowercase(Locale.ROOT)) {
        "http" -> 80
        "https" -> 443
        else -> -1
    }

    private fun canonicalizeJson(map: Map<String, String>): String {
        val entries = map.toSortedMap().entries
        val stringBuilder = StringBuilder().also {
            it.append('{')
        }
        var first = true
        for ((key, value) in entries) {
            if (!first) stringBuilder.append(',') else first = false
            stringBuilder.append('"').append(escapeJson(key)).append('"').append(':')
            stringBuilder.append('"').append(escapeJson(value)).append('"')
        }
        stringBuilder.append('}')
        return stringBuilder.toString()
    }

    private fun escapeJson(json: String): String = buildString(json.length) {
        for (jsonChar in json) when (jsonChar) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(jsonChar)
        }
    }

    private fun base64UrlEncode(bytes: ByteArray): String =
        Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}