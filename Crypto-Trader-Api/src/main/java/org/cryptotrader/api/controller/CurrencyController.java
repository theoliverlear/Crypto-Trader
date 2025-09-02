package org.cryptotrader.api.controller;

import org.cryptotrader.comm.response.DisplayCurrencyListResponse;
import org.cryptotrader.comm.response.TimeValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.cryptotrader.comm.request.AssetValueRequest;
import org.cryptotrader.comm.response.AssetValueResponse;
import org.cryptotrader.entity.currency.Currency;
import org.cryptotrader.api.service.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private CurrencyService currencyService;
    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @RequestMapping("/value")
    public ResponseEntity<AssetValueResponse> getCurrencyValue(@RequestBody AssetValueRequest assetValueRequest) {
        Currency currency = this.currencyService.getCurrencyByCurrencyCode(assetValueRequest.getCurrencyCode());
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            double assetValue = currency.getValue() * assetValueRequest.getShares();
            return new ResponseEntity<>(new AssetValueResponse(assetValue), HttpStatus.OK);
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
