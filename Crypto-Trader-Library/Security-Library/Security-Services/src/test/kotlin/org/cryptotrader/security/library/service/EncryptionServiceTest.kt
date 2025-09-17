package org.cryptotrader.security.library.service

import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@DisplayName("Encryption Service")
class EncryptionServiceTest : CryptoTraderTest() {
    private lateinit var encryptionService: EncryptionService

    @BeforeEach
    fun setUp() {
        this.encryptionService = EncryptionService("classpath:security/tink/test-aead.json")
    }
    
    @Test
    @DisplayName("Should obfuscate a given input")
    fun encrypt_ObfuscatesInput() {
        val testText = "Your secret data"
        val testTextBytes: ByteArray = testText.toByteArray(Charsets.UTF_8)
        val encryptedBytes: ByteArray = this.encryptionService.encrypt(testTextBytes)
        assertNotEquals(testTextBytes, encryptedBytes)
    }

    @Test
    @DisplayName("Should decrypt a given input")
    fun decrypt_DecryptsInput() {
        val testText = "Your secret data"
        val testTextBytes: ByteArray = testText.toByteArray(Charsets.UTF_8)
        val encryptedBytes: ByteArray = this.encryptionService.encrypt(testTextBytes)
        val decryptedBytes: ByteArray = this.encryptionService.decrypt(encryptedBytes)
        val decryptedText: String = decryptedBytes.toString(Charsets.UTF_8)
        assertEquals(testText, decryptedText)
    }
}