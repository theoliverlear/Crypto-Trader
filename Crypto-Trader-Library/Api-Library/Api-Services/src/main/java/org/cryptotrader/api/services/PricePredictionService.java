package org.cryptotrader.api.services;

import org.cryptotrader.api.services.CurrencyService;
import org.cryptotrader.comm.request.PricePredictionRequest;
import org.cryptotrader.entity.currency.Currency;
import org.cryptotrader.entity.prediction.PricePrediction;
import org.cryptotrader.repository.PricePredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PricePredictionService {
    private CurrencyService currencyService;
    private PricePredictionRepository pricePredictionRepository;
    @Autowired
    public PricePredictionService(CurrencyService currencyService,
                                  PricePredictionRepository pricePredictionRepository) {
        this.currencyService = currencyService;
        this.pricePredictionRepository = pricePredictionRepository;
    }
    public PricePrediction savePrediction(PricePredictionRequest pricePredictionRequest) {
        Currency currency = this.currencyService.getCurrencyByCurrencyCode(pricePredictionRequest.getCurrencyCode());
        PricePrediction pricePrediction = PricePrediction.builder()
                .currencyCode(pricePredictionRequest.getCurrencyCode())
                .currencyName(currency.getName())
                .predictedPrice(pricePredictionRequest.getPredictedPrice())
                .actualPrice(pricePredictionRequest.getActualPrice())
                .priceDifference(pricePredictionRequest.getPriceDifference())
                .percentDifference(pricePredictionRequest.getPercentDifference())
                .modelType(pricePredictionRequest.getModelType())
                .numRows(pricePredictionRequest.getNumRows())
                .lastUpdated(pricePredictionRequest.getLastUpdated())
                .build();
        return this.pricePredictionRepository.save(pricePrediction);
    }

    public PricePrediction getById(Long id) {
        return this.pricePredictionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Price prediction not found with id: " + id));
    }
}
