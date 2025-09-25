package org.cryptotrader.api.library.model

import org.cryptotrader.api.library.model.currency.PerformanceRating
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("PerformanceRating")
@Tag("model")
@DisplayName("Performance Rating")
class PerformanceRatingTest : CryptoTraderTest() {
    
    @Nested
    @DisplayName("fromValues")
    inner class FromValues {
        @Test
        @DisplayName("Should return rating with valid values")
        fun fromValues_ReturnValidRating_WithValidValues() {
            var rating = PerformanceRating.fromValues(100.0, 100.0)
            assertEquals(rating, PerformanceRating.NEUTRAL)

            rating = PerformanceRating.fromValues(100.0, 99.0)
            assertEquals(rating, PerformanceRating.DOWN)

            rating = PerformanceRating.fromValues(99.0, 100.0)
            assertEquals(rating, PerformanceRating.UP)
        }
    }
}