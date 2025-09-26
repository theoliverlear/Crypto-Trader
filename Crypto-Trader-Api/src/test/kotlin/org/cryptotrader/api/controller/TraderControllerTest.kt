package org.cryptotrader.api.controller

import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("TraderController")
@Tag("controller")
@DisplayName("Trader Controller")
class TraderControllerTest : CryptoTraderTest() {

    private lateinit var controller: TraderController

    @BeforeEach
    fun setUp() {
        this.controller = TraderController()
    }

    @Nested
    @Tag("initialization")
    @DisplayName("Initialization")
    inner class Initialization {
        @Test
        @DisplayName("Should construct controller")
        fun initialization_Constructs() {
            assertNotNull(controller)
        }
    }
}
