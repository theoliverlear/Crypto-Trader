package org.cryptotrader.security.library.service

import inet.ipaddr.IPAddressString
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("In-Memory IP Ban Service")
class InMemoryIpBanServiceTest : CryptoTraderTest() {
    private lateinit var ipBanService: InMemoryIpBanService
    
    @BeforeEach
    fun setUp() {
        this.ipBanService = InMemoryIpBanService()
    }

    @Test
    @DisplayName("Should store banned IPs")
    fun blockedAddresses_StoresBannedIPs() {
        val testIp = "0.0.0.0"
        this.ipBanService.blockedAddresses[testIp] = IPAddressString(testIp)
        assertTrue(this.ipBanService.blockedAddresses.containsKey(testIp))
    }

    @Test
    @DisplayName("Should detect blocked IPs")
    fun isBanned_DetectsBannedIPs() {
        val testIp = "0.0.0.0"
        this.ipBanService.blockedAddresses[testIp] = IPAddressString(testIp)
        assertTrue(this.ipBanService.isBanned(testIp))
    }

    @Test
    @DisplayName("Should ban IPs")
    fun ban_BansIPs() {
        val testIp = "0.0.0.0"
        this.ipBanService.ban(testIp)
        assertTrue(this.ipBanService.isBanned(testIp))
    }

    @Test
    @DisplayName("Should unban IPs that are banned")
    fun unban_UnbansIps_WhenBanned() {
        val testIp = "0.0.0.0"
        this.ipBanService.ban(testIp)
        assertTrue(this.ipBanService.isBanned(testIp))
        this.ipBanService.unban(testIp)
        assertFalse(this.ipBanService.isBanned(testIp))
    }
}