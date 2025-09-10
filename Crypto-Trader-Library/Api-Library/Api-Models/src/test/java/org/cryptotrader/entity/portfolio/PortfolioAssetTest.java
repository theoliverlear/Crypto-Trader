package org.cryptotrader.entity.portfolio;

import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Portfolio Asset")
public class PortfolioAssetTest extends CryptoTraderTest {
    private Currency testCurrency;
    private PortfolioAsset testAsset;
    @BeforeEach
    void setUp() {
        this.testCurrency = Currency.builder()
                .name("Test Coin")
                .currencyCode("TEST")
                .urlPath(Currency.TESTING_URL)
                .value(100)
                .build();
        this.testAsset = new PortfolioAsset(this.testCurrency, 0, 0);
    }
    
    @Test
    @DisplayName("Should be sellable with shares")
    public void testShouldBeSellableWithShares() {
        this.testAsset.setShares(1);
        this.testAsset.setTargetPrice(100);
        boolean expected = true;
        assertEquals(expected, this.testAsset.canSell());
    }
    
    @Test
    @DisplayName("Should not be sellable without shares")
    public void testShouldNotBeSellableWithoutShares() {
        this.testAsset.setShares(0);
        this.testAsset.setTargetPrice(100);
        boolean expected = false;
        assertEquals(expected, this.testAsset.canSell());
    }
    
    @Test
    @DisplayName("Should be buyable with wallet dollars")
    public void testShouldBeBuyableWithWalletDollars() {
        this.testAsset.setAssetWalletDollars(100);
        this.testAsset.setTargetPrice(100);
        boolean expected = true;
        assertEquals(expected, this.testAsset.canBuy());
    }
    
    @Test
    @DisplayName("Should not be buyable with no wallet dollars")
    public void testShouldNotBeBuyableWithNoWalletDollars() {
        this.testAsset.setAssetWalletDollars(0);
    }
    
    @Test
    @DisplayName("Should calculate values of shares")
    public void testShouldCalculateValuesOfShares() {
        this.testAsset.setShares(1);
        this.testAsset.setTargetPrice(100);
        this.testAsset.updateValues();
        assertEquals(100, this.testAsset.getSharesValueInDollars());
        
        this.testAsset.setShares(2);
        this.testAsset.updateValues();
        assertEquals(200, this.testAsset.getSharesValueInDollars());
    }
    
    @Test
    @DisplayName("Should calculate total value of a wallet")
    public void testShouldCalculateTotalValueOfWallet() {
        this.testAsset.setAssetWalletDollars(100);
        this.testAsset.setTargetPrice(100);
        this.testAsset.setShares(1);
        this.testAsset.updateValues();
        assertEquals(200, this.testAsset.getTotalValueInDollars());
    }
}
