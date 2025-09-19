package org.cryptotrader.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.communication.response.DisplayCurrencyListResponse;
import org.cryptotrader.api.library.communication.response.PerformanceRatingResponse;
import org.cryptotrader.api.library.communication.response.TimeValueResponse;
import org.cryptotrader.api.library.model.currency.PerformanceRating;
import org.cryptotrader.api.library.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.cryptotrader.api.library.communication.request.AssetValueRequest;
import org.cryptotrader.api.library.communication.response.AssetValueResponse;
import org.cryptotrader.api.library.entity.currency.Currency;

import java.util.List;

@Slf4j
@RestController
@Profile("!docs")
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;
    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @PostMapping("/value")
    public ResponseEntity<AssetValueResponse> getCurrencyValue(@RequestBody AssetValueRequest assetValueRequest) {
        Currency currency = this.currencyService.getCurrencyByCurrencyCode(assetValueRequest.getCurrencyCode());
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            double assetValue = currency.getValue() * assetValueRequest.getShares();
            return new ResponseEntity<>(new AssetValueResponse(assetValue), HttpStatus.OK);
        }
    }
    @GetMapping("/performance/{currencyCode}")
    public ResponseEntity<PerformanceRatingResponse> getCurrencyPerformance(@PathVariable String currencyCode) {
        if (currencyCode == null || currencyCode.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            PerformanceRating performanceRatingResponse = this.currencyService.getDayPerformance(currencyCode);
            String changePercent = this.currencyService.getPercentageDayPerformance(currencyCode);
            log.info("Currency performance: {} - {}", performanceRatingResponse, changePercent);
            return new ResponseEntity<>(new PerformanceRatingResponse(performanceRatingResponse, changePercent),
                                        HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<DisplayCurrencyListResponse> getAll() {
        return new ResponseEntity<>(this.currencyService.getCurrencyValuesResponse(), HttpStatus.OK);
    }

    @GetMapping("/history/{code}")
    public ResponseEntity<List<TimeValueResponse>> getHistory(@PathVariable("code") String code,
                                                              @RequestParam(value = "hours", defaultValue = "24") int hours,
                                                              @RequestParam(value = "intervalSeconds", defaultValue = "60") int intervalSeconds) {
        List<TimeValueResponse> history = this.currencyService.getCurrencyHistory(code, hours, intervalSeconds);
        if (history == null || history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
