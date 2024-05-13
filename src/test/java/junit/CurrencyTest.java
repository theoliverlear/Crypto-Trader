package junit;
//=================================-Imports-==================================
import org.junit.jupiter.api.Test;
import org.theoliverlear.entity.currency.Currency;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrencyTest {
    //============================-Variables-=================================
    Currency testCurrency = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
    //==============================-Tests-===================================

    //------------------------Test-Null-Instances-----------------------------
    @Test
    public void testNullInstances() {
        boolean nameIsNull = this.testCurrency.getName() == null;
        boolean currencyCodeIsNull = this.testCurrency.getCurrencyCode() == null;
        boolean urlPathIsNull = this.testCurrency.getUrlPath() == null;
        boolean formattedValueIsNull = this.testCurrency.getFormattedValue() == null;
        assertFalse(nameIsNull);
        assertFalse(currencyCodeIsNull);
        assertFalse(urlPathIsNull);
        assertFalse(formattedValueIsNull);
    }
    //----------------------Test-Get-Value-From-Json--------------------------
    @Test
    public void testGetValueFromJson() {
        double value = this.testCurrency.getValue();
        System.out.println(value);
        assertTrue(value > 0);
    }
    //---------------------Test-Get-Currency-Api-Json-------------------------
    @Test
    public void testGetCurrencyApiJson() {
        String json = this.testCurrency.getCurrencyApiJson();
        System.out.println(json);
    }
}
