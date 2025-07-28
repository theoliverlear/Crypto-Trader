package org.theoliverlear.entity.training.builder.models;

import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.prediction.ModelType;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.entity.training.specs.QueryLoad;
import org.theoliverlear.entity.training.specs.TrainingDevice;
import org.theoliverlear.entity.training.specs.TrainingQueryType;
import org.theoliverlear.entity.training.TrainingSession;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractTrainingSession implements BuilderFactory<TrainingSession> {
    public abstract AbstractTrainingSession currency(Currency currency);
    public abstract AbstractTrainingSession currency(String currencyCode);

    public abstract AbstractTrainingSession prediction(PricePrediction prediction);
    public abstract AbstractTrainingSession prediction(Long predictionId);

    public abstract AbstractTrainingSession numRows(int numRows);

    public abstract AbstractTrainingSession epochsTrained(int epochsTrained);

    public abstract AbstractTrainingSession maxEpochs(int maxEpochs);

    public abstract AbstractTrainingSession startingLoss(double startingLoss);

    public abstract AbstractTrainingSession finalLoss(double finalLoss);

    public abstract AbstractTrainingSession modelType(ModelType modelType);
    public abstract AbstractTrainingSession modelType(String modelType);

    public abstract AbstractTrainingSession queryType(TrainingQueryType queryType);
    public abstract AbstractTrainingSession queryType(String queryType);

    public abstract AbstractTrainingSession trainingStartTime(LocalDateTime startTime);
    public abstract AbstractTrainingSession trainingStartTime(String startTime);

    public abstract AbstractTrainingSession trainingEndTime(LocalDateTime endTime);
    public abstract AbstractTrainingSession trainingEndTime(String endTime);

    public abstract AbstractTrainingSession queryStartTime(LocalDateTime startTime);
    public abstract AbstractTrainingSession queryStartTime(String startTime);

    public abstract AbstractTrainingSession queryEndTime(LocalDateTime endTime);
    public abstract AbstractTrainingSession queryEndTime(String endTime);

    public abstract AbstractTrainingSession sequenceLength(int sequenceLength);

    public abstract AbstractTrainingSession batchSize(int batchSize);

    public abstract AbstractTrainingSession dimensionWidth(int dimensionWidth);

    public abstract AbstractTrainingSession queryLoad(QueryLoad queryLoad);
    public abstract AbstractTrainingSession queryLoad(String queryLoad);

    public abstract AbstractTrainingSession queryBatchSize(Integer queryBatchSize);

    public abstract AbstractTrainingSession trainingDevice(TrainingDevice trainingDevice);
    public abstract AbstractTrainingSession trainingDevice(String trainingDevice);
}
