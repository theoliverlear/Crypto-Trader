package org.theoliverlear.entity.prediction.builder.models;

import org.theoliverlear.entity.prediction.ModelType;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractPricePrediction implements BuilderFactory<PricePrediction> {
    public abstract AbstractPricePrediction currencyCode(String currencyCode);
    public abstract AbstractPricePrediction currencyName(String currencyName);
    public abstract AbstractPricePrediction predictedPrice(double predictedPrice);
    public abstract AbstractPricePrediction actualPrice(double actualPrice);
    public abstract AbstractPricePrediction priceDifference(double priceDifference);
    public abstract AbstractPricePrediction percentDifference(double percentDifference);
    public abstract AbstractPricePrediction lastUpdated(LocalDateTime localDateTime);
    public abstract AbstractPricePrediction numRows(int numRows);
    public abstract AbstractPricePrediction modelType(String modelType);
    public abstract AbstractPricePrediction modelType(ModelType modelType);
}
