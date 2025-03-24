package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theoliverlear.comm.request.PricePredictionRequest;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.repository.PricePredictionRepository;


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
    public void savePrediction(PricePredictionRequest pricePredictionRequest) {
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
        this.pricePredictionRepository.save(pricePrediction);
    }
}
