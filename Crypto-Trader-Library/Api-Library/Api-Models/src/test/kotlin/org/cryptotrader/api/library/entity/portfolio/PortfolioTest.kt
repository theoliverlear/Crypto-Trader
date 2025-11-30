package org.cryptotrader.api.library.entity.portfolio

import org.assertj.core.api.Assertions.assertThat
import org.cryptotrader.data.library.entity.currency.Currency
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

@DisplayName("Portfolio")
class PortfolioTest : CryptoTraderTest() {
    private lateinit var bitcoinCurrency: Currency
    private lateinit var ethereumCurrency: Currency
    private lateinit var portfolio: Portfolio
    
    @BeforeEach
    fun setUp() {
        val testUser = ProductUser("cryptotrader", "password")
        this.bitcoinCurrency = Currency.builder().currencyCode("BTC").value(2.0).build()
        this.ethereumCurrency = Currency.builder().currencyCode("ETH").value(1.0).build()
        this.portfolio = Mockito.spy(Portfolio(testUser))
    }

    @Test
    @DisplayName("Should accept new assets")
    fun addAsset_AcceptsAssets() {
        val bitcoinAsset = PortfolioAsset(this.bitcoinCurrency, 0.0, 0.0)
        val ethereumAsset = PortfolioAsset(this.ethereumCurrency, 0.0, 0.0)
        assertThat(this.portfolio.assets).isEmpty()
        this.portfolio.addAsset(bitcoinAsset)
        assertEquals(this.portfolio.assets.size, 1)
        this.portfolio.addAsset(ethereumAsset)
        assertEquals(this.portfolio.assets.size, 2)
    }

    @Test
    @DisplayName("Should update portfolio value correctly")
    fun updateValues_UpdatesValueCorrectly() {
        val bitcoinWalletDollars = 100.0
        val testEthereumShares = 10.0
        val bitcoinAsset = PortfolioAsset(this.bitcoinCurrency, 0.0, bitcoinWalletDollars)
        val ethereumAsset = PortfolioAsset(this.ethereumCurrency, testEthereumShares, 0.0)
        val expectedEthereumValue: Double = testEthereumShares * this.ethereumCurrency.value
        this.portfolio.addAsset(bitcoinAsset)
        verify(this.portfolio, Mockito.times(1)).updateValues()
        this.portfolio.addAsset(ethereumAsset)
        verify(this.portfolio, Mockito.times(2)).updateValues()
        val expectedValue = bitcoinWalletDollars + expectedEthereumValue
        assertEquals(expectedValue, this.portfolio.totalWorth)
    }

    @Test
    @DisplayName("Should return portfolio value correctly")
    fun getTotalPortfolioWorth_ReturnsValueCorrectly() {
        val bitcoinWalletDollars = 100.0
        val testEthereumShares = 10.0
        val bitcoinAsset = PortfolioAsset(this.bitcoinCurrency, 0.0, bitcoinWalletDollars)
        val ethereumAsset = PortfolioAsset(this.ethereumCurrency, testEthereumShares, 0.0)
        val assetsLists: List<PortfolioAsset> = listOf(bitcoinAsset, ethereumAsset)
        val expectedPortfolioValue = 110.0
        val actualPortfolioValue = Portfolio.getTotalPortfolioValue(assetsLists)
        assertEquals(expectedPortfolioValue, actualPortfolioValue)
    }
}
