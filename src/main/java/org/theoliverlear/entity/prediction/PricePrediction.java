package org.theoliverlear.entity.prediction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "predictions")
public class PricePrediction {
    @Id
    private Long id;
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
    @Column(name = "last_updated")
    private String lastUpdated;
    @Column(name = "step_size")
    private int stepSize;
    public PricePrediction(String currencyCode,
                           String currencyName,
                           double predictedPrice,
                           double actualPrice,
                           double priceDifference,
                           double percentDifference,
                           String lastUpdated,
                           int stepSize) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.predictedPrice = predictedPrice;
        this.actualPrice = actualPrice;
        this.priceDifference = priceDifference;
        this.percentDifference = percentDifference;
        this.lastUpdated = lastUpdated;
        this.stepSize = stepSize;

    }
}
