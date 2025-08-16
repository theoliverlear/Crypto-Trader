package org.theoliverlear.entity.currency;
//=================================-Imports-==================================
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SupportedCurrencies {
    //============================-Constants-=================================
    public static final List<Currency> SUPPORTED_CURRENCIES = new ArrayList<>();
    private static final Map<String, Currency> CURRENCY_MAP = new HashMap<>();
    private static final String JSON_FILE_PATH = "src/main/resources/static/currencies.json";
    public static final Currency BITCOIN = new Currency("Bitcoin", "BTC");
    //==========================-Static-Actions-==============================
    static {
        loadCurrenciesFromJson();
    }
    public static void loadCurrenciesFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (!jsonFile.exists()) {
                log.error("Currency JSON file not found. Using empty list.");
                return;
            }
            loadCurrenciesFromJson(objectMapper, jsonFile);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load currencies from JSON", exception);
        }
    }

    private static void loadCurrenciesFromJson(ObjectMapper objectMapper, File jsonFile) throws IOException {
        List<Map<String, String>> currencyData = objectMapper.readValue(jsonFile, new TypeReference<>() {});
        for (Map<String, String> entry : currencyData) {
            String name = entry.get("name");
            String code = entry.get("code");
            Currency currency;
            try {
                currency = new Currency(name, code);
            } catch (IllegalStateException exception) {
                continue;
            }
            if (name.equals("Bitcoin")) {
                SUPPORTED_CURRENCIES.add(BITCOIN);
                CURRENCY_MAP.put(code, BITCOIN);
            } else {
                SUPPORTED_CURRENCIES.add(currency);
                CURRENCY_MAP.put(code, currency);
            }
            log.info("Loaded currency: {} ({})", name, code);
        }
        log.info("Loaded {} currencies from JSON.", SUPPORTED_CURRENCIES.size());
    }
}
