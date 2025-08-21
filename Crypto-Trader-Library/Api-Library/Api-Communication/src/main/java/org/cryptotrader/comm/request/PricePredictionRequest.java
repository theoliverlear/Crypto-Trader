package org.cryptotrader.comm.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PricePredictionRequest {
    private String currencyCode;
    private double predictedPrice;
    private double actualPrice;
    private double priceDifference;
    private double percentDifference;
    private String modelType;
    private int numRows;
    private LocalDateTime lastUpdated;
}
