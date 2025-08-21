package org.cryptotrader.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CurrencyJsonGenerator {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String EXCHANGE_API_URL = "https://api.exchange.coinbase.com/currencies";
    private static final String CURRENCY_RATES_URL = "https://api.coinbase.com/v2/exchange-rates?currency=USD";
    private static final Path OUTPUT_PATH = Paths.get("src/main/resources/static/currencies.json");

    @Autowired
    public CurrencyJsonGenerator(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private <T> T fetchJson(String url, ParameterizedTypeReference<T> typeRef) {
        return this.restTemplate.exchange(url, HttpMethod.GET, null, typeRef).getBody();
    }

    public List<Map<String, Object>> getCurrencies() {
        List<Map<String, Object>> currencies = fetchJson(EXCHANGE_API_URL,
                new ParameterizedTypeReference<>() { });

        Map<String, Object> exchangeRatesRoot = fetchJson(CURRENCY_RATES_URL, 
                new ParameterizedTypeReference<>() { });
        
        Map<String, Object> data = asMap(exchangeRatesRoot.get("data"));
        Map<String, Object> rates = asMap(data.get("rates"));

        Set<String> currenciesToSkip = new HashSet<>(Arrays.asList(
                "DYP", "USD", "LQTY", "WLUNA", "GUSD",
                "DAI", "ME", "MASK", "USDC", "DAR",
                "AERGO", "TONE", "RAD", "NU"
        ));

        List<Map<String, Object>> matchedCurrencies = new ArrayList<>();
        if (currencies != null) {
            for (Map<String, Object> currency : currencies) {
                String id = this.asString(currency.get("id"));
                String name = this.asString(currency.get("name"));
                if (id == null || name == null) {
                    continue;
                }
                if (rates.containsKey(id)) {
                    boolean containsDigit = id.chars().anyMatch(Character::isDigit);
                    if (!containsDigit && !currenciesToSkip.contains(id)) {
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("name", name);
                        entry.put("code", id);
                        matchedCurrencies.add(entry);
                    }
                }
            }
        }
        return matchedCurrencies;
    }

    public List<Map<String, Object>> getCachedCurrencies() throws IOException {
        if (!Files.exists(OUTPUT_PATH)) {
            throw new NoSuchFileException(OUTPUT_PATH.toString());
        }
        byte[] fileAsBytes = Files.readAllBytes(OUTPUT_PATH);
        return this.objectMapper.readValue(fileAsBytes, new TypeReference<>() { });
    }

    public void saveJson(List<Map<String, Object>> matchedCryptos) {
        try {
            Files.createDirectories(OUTPUT_PATH.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(OUTPUT_PATH, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, matchedCryptos);
            }
            log.info("Matched cryptocurrencies saved to {}", OUTPUT_PATH);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save JSON to " + OUTPUT_PATH, exception);
        }
    }

    public List<String> getAllCurrencyCodes(boolean useCache) {
        List<Map<String, Object>> currencies;
        if (useCache) {
            try {
                currencies = this.getCachedCurrencies();
            } catch (NoSuchFileException e) {
                log.error("Cache file not found. Fetching currencies from API.");
                currencies = this.getCurrencies();
            } catch (IOException e) {
                log.error("Failed to read cache. Fetching currencies from API.");
                currencies = this.getCurrencies();
            }
        } else {
            currencies = getCurrencies();
        }

        List<String> codes = currencies.stream()
                .map(codeMap -> asString(codeMap.get("code")))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.info("Number of currencies found: {}", codes.size());
        return codes;
    }
    
    public void generateAndSave() {
        this.saveJson(this.getCurrencies());
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object object) {
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        }
        return Collections.emptyMap();
    }

    private String asString(Object object) {
        if (object == null) {
            return null;
        }
        return String.valueOf(object);
    }

    public static CurrencyJsonGenerator standalone() {
        return new CurrencyJsonGenerator(new RestTemplate(), new ObjectMapper());
    }
}