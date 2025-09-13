package org.cryptotrader.data.controller;

import org.cryptotrader.api.library.services.CurrencyService;
import org.cryptotrader.api.library.communication.response.DisplayCurrencyListResponse;
import org.cryptotrader.api.library.communication.response.TimeValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Profile("!docs")
@RequestMapping("/data/currency")
public class CurrencyController {
    private CurrencyService currencyService;
    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
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
