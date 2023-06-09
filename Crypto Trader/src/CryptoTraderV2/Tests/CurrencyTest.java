package CryptoTraderV2.Tests;

import CryptoTraderV2.Currency;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyTest {
    Currency testBitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
    @Test
    public void testGetName() {
        assertEquals("Bitcoin", testBitcoin.getName());
    }
    @Test
    public void testGetCurrencyCode() {
        assertEquals("BTC", testBitcoin.getCurrencyCode());
    }
    @org.junit.jupiter.api.Test
    public void testGetFormattedValue() {
        // Test against Bitcoin's current value
        String formattedValue = testBitcoin.getFormattedValue();
        String[] formattedValueSplit = formattedValue.split("\\.");
        int formatLeftSide = formattedValueSplit[0].replace("$", "").replace(",", "").length();
        int formatRightSide = formattedValueSplit[1].length();
        assertEquals(5, formatLeftSide);
        assertEquals(8, formatRightSide);
    }
}
