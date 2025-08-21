package org.cryptotrader.entity.prediction;

import jakarta.persistence.*;
import org.cryptotrader.entity.Identifiable;
import org.cryptotrader.entity.prediction.builder.PricePredictionBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
public class PricePrediction extends Identifiable {
    @Column(name = "currency_code")
    private String currencyCode;
    @Column(name = "currency_name")
    private String currencyName;
    @Column(name = "predicted_price")
    private double predictedPrice;
    @Column(name = "actual_price")
    private double actualPrice;
    @Column(name = "price_difference")
    private double priceDifference;
    @Column(name = "percent_difference")
    private double percentDifference;
    @Column(name = "model_type")
    @Enumerated(EnumType.STRING)
    private ModelType modelType;
    @Column(name = "num_rows")
    private int numRows;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    public PricePrediction() {
        this.currencyCode = "";
        this.currencyName = "";
        this.predictedPrice = 0.0;
        this.actualPrice = 0.0;
        this.priceDifference = 0.0;
        this.percentDifference = 0.0;
        this.modelType = ModelType.LSTM;
        this.numRows = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    public PricePrediction(String currencyCode,
                           String currencyName,
                           double predictedPrice,
                           double actualPrice,
                           double priceDifference,
                           double percentDifference,
                           ModelType modelType,
                           int numRows,
                           LocalDateTime lastUpdated) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.predictedPrice = predictedPrice;
        this.actualPrice = actualPrice;
        this.priceDifference = priceDifference;
        this.percentDifference = percentDifference;
        this.modelType = modelType;
        this.numRows = numRows;
        this.lastUpdated = lastUpdated;
    }
    public static PricePredictionBuilder builder() {
        return new PricePredictionBuilder();
    }
}
