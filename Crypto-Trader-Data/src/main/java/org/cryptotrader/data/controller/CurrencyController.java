package org.cryptotrader.data.controller;

import org.cryptotrader.api.library.communication.request.AssetValueRequest;
import org.cryptotrader.api.library.communication.response.AssetValueResponse;
import org.cryptotrader.api.library.communication.response.PerformanceRatingResponse;
import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.model.currency.PerformanceRating;
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
}
