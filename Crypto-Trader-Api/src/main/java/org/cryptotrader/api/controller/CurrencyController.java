package org.cryptotrader.api.controller;

import org.cryptotrader.api.library.communication.response.PerformanceRatingResponse;
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

@RestController
@Profile("!docs")
@RequestMapping("/api/currency")
public class CurrencyController {
    private CurrencyService currencyService;
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
        if (currencyCode == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            PerformanceRating performanceRatingResponse = this.currencyService.getDayPerformance(currencyCode);
            return new ResponseEntity<>(new PerformanceRatingResponse(performanceRatingResponse),
                                        HttpStatus.OK);
        }
    }
}
