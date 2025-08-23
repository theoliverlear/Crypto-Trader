package org.cryptotrader.api.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.cryptotrader.api.service.PricePredictionService;
import org.cryptotrader.entity.prediction.PricePrediction;
import org.cryptotrader.entity.prediction.PricePredictionLookup;

@Component
@RequiredArgsConstructor
public class PricePredictionLookupAdapter implements PricePredictionLookup {
    @Autowired
    private final PricePredictionService pricePredictionService;

    @Override
    public PricePrediction getById(Long id) {
        return this.pricePredictionService.getById(id);
    }
}
