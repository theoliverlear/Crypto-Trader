package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.services.jwt.RefreshTokenService
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Refresh Token Service")
class RefreshTokenServiceTest : CryptoTraderTest() {

    @Test
    @DisplayName("Rotates refresh token on use and detects reuse")
    fun rotateAndDetectReuse() {
        val service = RefreshTokenService("__Host-rt", 30, true)
        val issue = service.issue(123L, "jkt-thumb")
        // Happy path rotation
        val rotated = service.validateAndRotate(issue.id, "jkt-thumb")
        assertNotNull(rotated.newRecord)
        val newId = rotated.newRecord!!.id
        assertNotEquals(issue.id, newId)
        // Reusing the original should revoke family
        val reuse = service.validateAndRotate(issue.id, "jkt-thumb")
        assertNull(reuse.newRecord)
        assertTrue(reuse.reuseDetected)
    }

    @Test
    @DisplayName("Rejects when jkt mismatches on refresh")
    fun rejectOnJktMismatch() {
        val service = RefreshTokenService("__Host-rt", 30, true)
        val issue = service.issue(123L, "expected-jkt")
        val res = service.validateAndRotate(issue.id, "wrong-jkt")
        assertNull(res.newRecord)
        assertTrue(res.reuseDetected)
    }
}
