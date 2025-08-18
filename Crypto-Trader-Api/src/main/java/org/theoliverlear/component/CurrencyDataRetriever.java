package org.theoliverlear.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.currency.SupportedCurrencies;
import org.theoliverlear.model.http.ApiDataRetriever;

import java.io.IOException;
import java.util.*;

@Component
public class CurrencyDataRetriever extends ApiDataRetriever {
    private static final String API_URL = "https://api.coinbase.com/v2/exchange-rates?currency=USD";
    public CurrencyDataRetriever() {
        super(API_URL);
    }
    public Map<String ,Currency> getUpdatedCurrencies() {
        Map<String, Double> currencyMap = this.getCurrencyMap();
        Map<String, Currency> updatedCurrencyMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : currencyMap.entrySet()) {
            String currencyCode = entry.getKey();
            double value = entry.getValue();
            Currency currency = Currency.builder()
                    .currencyCode(currencyCode)
                    .value(value)
                    .build();
            updatedCurrencyMap.put(currencyCode, currency);
        }
        List<String> currencyMapKeys = new ArrayList<>(updatedCurrencyMap.keySet());
        List<String> supportedCurrenciesKeys = SupportedCurrencies.SUPPORTED_CURRENCIES.stream().map(Currency::getCurrencyCode).toList();
        Map<String, Currency> filteredCurrencyMap = new HashMap<>();
        for (String currencyCode : supportedCurrenciesKeys) {
            if (currencyMapKeys.contains(currencyCode)) {
                filteredCurrencyMap.put(currencyCode, updatedCurrencyMap.get(currencyCode));
            }
        }
        return filteredCurrencyMap;
    }
    public Map<String, Double> getCurrencyMap() {
        Map<String, Double> currencyMap = new HashMap<>();
        this.fetchResponse();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(this.getResponse());
            JsonNode ratesNode = rootNode.path("data").path("rates");
            boolean validNode = ratesNode != null && ratesNode.isObject();
            if (validNode) {
                Iterator<String> fieldNames = ratesNode.fieldNames();
                while (fieldNames.hasNext()) {
                    String code = fieldNames.next();
                    double value = 1.0 / ratesNode.get(code).asDouble();
                    currencyMap.put(code, value);
                }
            } else {
                System.err.println("Error: Invalid data format received from API.");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to parse JSON response", exception);
        }
        return currencyMap;
    }
}
