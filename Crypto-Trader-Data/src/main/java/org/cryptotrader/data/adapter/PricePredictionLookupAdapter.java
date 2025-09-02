package org.cryptotrader.data.adapter;

import lombok.RequiredArgsConstructor;
import org.cryptotrader.api.services.PricePredictionService;
import org.cryptotrader.entity.prediction.PricePrediction;
import org.cryptotrader.entity.prediction.PricePredictionLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
