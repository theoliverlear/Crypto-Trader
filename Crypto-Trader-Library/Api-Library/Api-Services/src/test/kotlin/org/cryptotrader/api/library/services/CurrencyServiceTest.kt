package org.cryptotrader.api.library.services

import org.cryptotrader.test.CryptoTraderTest
import org.cryptotrader.api.library.repository.*
import org.cryptotrader.api.library.component.*
import org.cryptotrader.api.library.entity.currency.Currency
import org.cryptotrader.api.library.entity.currency.CurrencyHistory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@DisplayName("Currency Service")
class CurrencyServiceTest : CryptoTraderTest() {
    @InjectMocks
    lateinit var currencyService: CurrencyService

    @Mock
    lateinit var currencyRepository: CurrencyRepository

    @Mock
    lateinit var currencyHistoryRepository: CurrencyHistoryRepository

    @Mock
    lateinit var uniqueCurrencyRepository: UniqueCurrencyRepository

    @Mock
    lateinit var uniqueCurrencyHistoryRepository: UniqueCurrencyHistoryRepository

    @Mock
    lateinit var currencyDataRetriever: CurrencyDataRetriever

    @Mock
    lateinit var backfiller: MarketSnapshotsBackfiller

    @Mock
    lateinit var snapshotService: MarketSnapshotService

    @Mock
    lateinit var currencyJsonGenerator: CurrencyJsonGenerator

    companion object {
        val topTenCurrencies: Array<String> = arrayOf("BTC", "ETH", "LTC", "BCH", "XRP", "EOS", "BNB", "ETC", "ZEC", "XMR")
        val topTenCurrenciesList: List<Currency> = arrayListOf<Currency>(
            Currency.builder().currencyCode("BTC").value(1.0).build(),
            Currency.builder().currencyCode("ETH").value(1.0).build(),
            Currency.builder().currencyCode("LTC").value(1.0).build(),
            Currency.builder().currencyCode("BCH").value(1.0).build(),
            Currency.builder().currencyCode("XRP").value(1.0).build(),
            Currency.builder().currencyCode("EOS").value(1.0).build(),
            Currency.builder().currencyCode("BNB").value(1.0).build(),
            Currency.builder().currencyCode("ETC").value(1.0).build(),
            Currency.builder().currencyCode("ZEC").value(1.0).build(),
            Currency.builder().currencyCode("XMR").value(1.0).build()
        )
    }

    @Test
    @DisplayName("Should get the top ten currencies with ten or more" +
            " currencies in the database")
    fun getTopTenCurrencies_GetsTopTenCurrencies_DatabaseHasTenCurrencies() {
        `when`(this.currencyRepository.findTop10ByOrderByValueDesc()).thenReturn(
            topTenCurrenciesList
        )
        val actualCurrencyList: List<Currency> = this.currencyService.topTenCurrencies
        val actualSize: Int = actualCurrencyList.size
        val expectedSize = 10
        assertEquals(expectedSize, actualSize)

        assertEquals(actualCurrencyList, topTenCurrenciesList)
    }

    @Test
    @DisplayName("Should save currencies when value changes")
    fun saveCurrencyIfNew_SavesCurrencies_ValueChanges() {
        val currency: Currency = Currency.builder().currencyCode("BTC").value(2.0).build()
        val previousCurrency: Currency = Currency.builder().currencyCode("BTC").value(1.0).build()
        val updatedCurrency: Currency = Currency.builder().currencyCode("BTC").value(2.0).build()
        this.currencyService.saveCurrencyIfNew(currency, previousCurrency, updatedCurrency)
        verify(this.currencyRepository).save(currency)
        verify(this.currencyHistoryRepository).save(Mockito.isA(CurrencyHistory::class.java))
    }
}