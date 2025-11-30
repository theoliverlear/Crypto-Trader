package org.cryptotrader.api.library.trade;


import org.cryptotrader.data.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.model.trade.AssetTrader;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Asset Trader")
public class AssetTraderTest extends CryptoTraderTest {
    private Currency testCurrency;
    private PortfolioAsset testAsset;
    private AssetTrader testTrader;
    @BeforeEach
    void setUp() {
        this.testCurrency = Currency.builder()
                                    .name("Test Coin")
                                    .currencyCode("TEST")
                                    .urlPath(Currency.TESTING_URL)
                                    .value(100)
                                    .build();
        this.testAsset = new PortfolioAsset(this.testCurrency,0, 0);
        this.testTrader = new AssetTrader(this.testAsset);
    }
    
    @Test
    @DisplayName("Should sell at an increased price")
    public void testShouldSell() {
        this.testAsset.setShares(1);
        this.testAsset.setTargetPrice(100);
        boolean expected = false;
        assertEquals(expected, this.testTrader.trade());
        this.testCurrency.setValue(101);
        expected = true;
        assertEquals(expected, this.testTrader.trade());
    }
    
    @Test
    @DisplayName("Should buy at a decreased price")
    public void testShouldBuy() {
        this.testAsset.setShares(0);
        this.testAsset.setAssetWalletDollars(100);
        this.testAsset.setTargetPrice(100);
        boolean expected = false;
        assertEquals(expected, this.testTrader.trade());
        this.testCurrency.setValue(99);
        expected = true;
        assertEquals(expected, this.testTrader.trade());
    }
    
    @Test
    @DisplayName("Selling should empty shares")
    public void testShouldEmptyShares() {
        this.testAsset.setShares(1);
        this.testAsset.setTargetPrice(100);
        this.testTrader.sell(this.testCurrency.getValue());
        assertEquals(0, this.testAsset.getShares());
    }
    
    @Test
    @DisplayName("Buying should add shares")
    public void testShouldAddShares() {
        this.testAsset.setShares(0);
        this.testAsset.setAssetWalletDollars(100);
        this.testAsset.setTargetPrice(100);
        this.testTrader.buy(this.testCurrency.getValue());
        assertEquals(1, this.testAsset.getShares());
    }
}
