package org.cryptotrader.api.controller

import org.cryptotrader.api.library.communication.request.AssetValueRequest
import org.cryptotrader.api.library.communication.response.AssetValueResponse
import org.cryptotrader.api.library.communication.response.DisplayCurrencyListResponse
import org.cryptotrader.api.library.communication.response.DisplayCurrencyResponse
import org.cryptotrader.api.library.communication.response.PerformanceRatingResponse
import org.cryptotrader.api.library.entity.currency.Currency
import org.cryptotrader.api.library.model.currency.PerformanceRating
import org.cryptotrader.api.library.services.CurrencyService
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Tag("CurrencyController")
@Tag("controller")
@DisplayName("Currency Controller")
class CurrencyControllerTest : CryptoTraderTest() {

    @Mock lateinit var currencyService: CurrencyService

    val testCode = "TEST"
    val testCurrencyName = "Test Currency"
    val testCurrency: Currency = Currency.builder()
                                         .currencyCode(this.testCode)
                                         .value(10.0)
                                         .name(this.testCurrencyName)
                                         .urlPath("test")
                                         .build()
    private lateinit var controller: CurrencyController

    @BeforeEach
    fun setUp() {
        this.controller = CurrencyController(currencyService)
    }

    @Nested
    @Tag("getCurrencyValue")
    @DisplayName("Get Currency Value")
    inner class GetCurrencyValue {
        @Test
        @DisplayName("Should calculate asset value for valid request")
        fun getCurrencyValue_CalculatesValue_ValidRequest() {
            val request = AssetValueRequest(testCode, 1.0)
            `when`(currencyService.getCurrencyByCurrencyCode(request.currencyCode)).thenReturn(testCurrency)
            val response: ResponseEntity<AssetValueResponse> = controller.getCurrencyValue(request)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(10.0, response.body!!.value)
        }

        @Test
        @DisplayName("Should return not found when currency missing")
        fun getCurrencyValue_ReturnsNotFound_WhenMissing() {
            val request = AssetValueRequest("MISSING", 1.0)
            `when`(currencyService.getCurrencyByCurrencyCode(request.currencyCode)).thenReturn(null)
            val response: ResponseEntity<AssetValueResponse> = controller.getCurrencyValue(request)
            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertEquals(null, response.body)
        }
    }

    @Nested
    @Tag("getCurrencyPerformance")
    @DisplayName("Get Currency Performance")
    inner class GetCurrencyPerformance {
        @Test
        @DisplayName("Should return performance and percent change for valid request")
        fun getCurrencyPerformance_ReturnsPerformance_ValidRequest() {
            `when`(currencyService.getDayPerformance(testCode)).thenReturn(
                PerformanceRating.UP)
            `when`(currencyService.getPercentageDayPerformance(testCode)).thenReturn("+%5.0")
            val response: ResponseEntity<PerformanceRatingResponse> = controller.getCurrencyPerformance(testCode)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(PerformanceRatingResponse(PerformanceRating.UP, "+%5.0"), response.body)
        }

        @Test
        @DisplayName("Should return bad request when code invalid")
        fun getCurrencyPerformance_ReturnsBadRequest_OnInvalidCode() {
            val invalidCode = "INVALID"
            `when`(currencyService.getDayPerformance(invalidCode)).thenReturn(null)
            val response: ResponseEntity<PerformanceRatingResponse> = controller.getCurrencyPerformance(invalidCode)
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
            assertEquals(null, response.body)
        }
    }

    @Nested
    @Tag("getAll")
    @DisplayName("Get All Currencies")
    inner class GetAll {
        @Test
        @DisplayName("Should return all currencies")
        fun getAll_ReturnsAllCurrencies() {
            val testDisplayCurrencyResponse = DisplayCurrencyListResponse(
                listOf(
                    DisplayCurrencyResponse("Test Currency", "TEST", 10.0, "test"),
                    DisplayCurrencyResponse("Another Currency", "ANOTHER", 20.0, "another")
                )
            )
            `when`(currencyService.getCurrencyValuesResponse()).thenReturn(testDisplayCurrencyResponse)
            val response: ResponseEntity<DisplayCurrencyListResponse> = controller.getAll()
            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(testDisplayCurrencyResponse, response.body)
        }

        @Test
        @DisplayName("Should return all currencies with offset")
        fun getAllWithOffset_ReturnsAll_WithOffset() { }
    }

    @Nested
    @Tag("getHistory")
    @DisplayName("Get History")
    inner class GetHistory {
        @Test
        @DisplayName("Should return history list or no content when empty")
        fun getHistory_ReturnsList_OrNoContent() { }
    }
}
