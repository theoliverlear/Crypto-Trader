package org.cryptotrader.entity.vendor;

import org.cryptotrader.api.library.entity.vendor.Vendor;
import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Vendor")
public class VendorTest extends CryptoTraderTest {
    private Vendor testVendor;
    @BeforeEach
    void setUp() {
        this.testVendor = new Vendor("Test Vendor", 0.1);
    }
    
    @Test
    @DisplayName("Should adjust prices")
    public void testShouldAdjustPrices() {
        double price = 100;
        double expectedPrice = 110;
        double actualPrice = this.testVendor.getAdjustedPrice(price);
        assertEquals(expectedPrice, actualPrice);
    }
}
