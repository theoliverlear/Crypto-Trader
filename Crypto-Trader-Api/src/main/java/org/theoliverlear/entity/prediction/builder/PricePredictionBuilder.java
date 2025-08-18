package org.theoliverlear.entity.prediction.builder;

import org.theoliverlear.entity.prediction.ModelType;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.entity.prediction.builder.models.AbstractPricePrediction;

import java.time.LocalDateTime;

public class PricePredictionBuilder extends AbstractPricePrediction {
    private String currencyCode;
    private String currencyName;
    private double predictedPrice;
    private double actualPrice;
    private double priceDifference;
    private double percentDifference;
    private LocalDateTime lastUpdated;
    private int numRows;
    private ModelType modelType;

    public PricePredictionBuilder() {
        this.currencyCode = "";
        this.currencyName = "";
        this.predictedPrice = 0.0;
        this.actualPrice = 0.0;
        this.percentDifference = 0.0;
        this.lastUpdated = LocalDateTime.now();
        this.numRows = 0;
        this.modelType = ModelType.LSTM;
    }

    @Override
    public AbstractPricePrediction currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    @Override
    public AbstractPricePrediction currencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    @Override
    public AbstractPricePrediction predictedPrice(double predictedPrice) {
        this.predictedPrice = predictedPrice;
        return this;
    }

    @Override
    public AbstractPricePrediction actualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
        return this;
    }

    @Override
    public AbstractPricePrediction priceDifference(double priceDifference) {
        this.priceDifference = priceDifference;
        return this;
    }

    @Override
    public AbstractPricePrediction percentDifference(double percentDifference) {
        this.percentDifference = percentDifference;
        return this;
    }

    @Override
    public AbstractPricePrediction lastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public AbstractPricePrediction numRows(int numRows) {
        this.numRows = numRows;
        return this;
    }

    @Override
    public AbstractPricePrediction modelType(String modelType) {
        this.modelType = ModelType.from(modelType);
        return this;
    }

    @Override
    public AbstractPricePrediction modelType(ModelType modelType) {
        this.modelType = modelType;
        return this;
    }

    @Override
    public PricePrediction build() {
        return new PricePrediction(this.currencyCode,
                                   this.currencyName,
                                   this.predictedPrice,
                                   this.actualPrice,
                                   this.priceDifference,
                                   this.percentDifference,
                                   this.modelType,
                                   this.numRows,
                                   this.lastUpdated);
    }
}
