package org.theoliverlear.entity.training.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.prediction.ModelType;
import org.theoliverlear.entity.prediction.PricePrediction;
import org.theoliverlear.entity.training.specs.QueryLoad;
import org.theoliverlear.entity.training.specs.TrainingDevice;
import org.theoliverlear.entity.training.specs.TrainingQueryType;
import org.theoliverlear.entity.training.TrainingSession;
import org.theoliverlear.entity.training.builder.models.AbstractTrainingSession;
import org.theoliverlear.service.PricePredictionService;

import java.time.LocalDateTime;

@Component
public class TrainingSessionBuilder extends AbstractTrainingSession {
    private Currency currency;
    private PricePrediction prediction;
    private int numRows;
    private int epochsTrained;
    private int maxEpochs;
    private double startingLoss;
    private double finalLoss;
    private ModelType modelType;
    private TrainingQueryType queryType;
    private LocalDateTime trainingStartTime;
    private LocalDateTime trainingEndTime;
    private LocalDateTime queryStartTime;
    private LocalDateTime queryEndTime;
    private int sequenceLength;
    private int batchSize;
    private int dimensionWidth;
    private QueryLoad queryLoad;
    private Integer queryBatchSize;
    private TrainingDevice trainingDevice;

    @Autowired
    private PricePredictionService pricePredictionService;

    public TrainingSessionBuilder() {
        this.currency = null;
        this.prediction = null;
        this.numRows = 0;
        this.epochsTrained = 0;
        this.maxEpochs = 0;
        this.startingLoss = 0.0;
        this.finalLoss = 0.0;
        this.modelType = null;
        this.queryType = null;
        this.trainingStartTime = null;
        this.trainingEndTime = null;
        this.queryStartTime = null;
        this.queryEndTime = null;
        this.sequenceLength = 0;
        this.batchSize = 0;
        this.dimensionWidth = 0;
        this.queryLoad = null;
        this.queryBatchSize = null;
        this.trainingDevice = null;
    }

    @Override
    public AbstractTrainingSession currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public AbstractTrainingSession currency(String currencyCode) {
        this.currency = Currency.fromExisting(currencyCode);
        return this;
    }

    @Override
    public AbstractTrainingSession prediction(PricePrediction prediction) {
        this.prediction = prediction;
        return this;
    }

    @Override
    public AbstractTrainingSession prediction(Long predictionId) {
        this.prediction = this.pricePredictionService.getById(predictionId);
        return this;
    }

    @Override
    public AbstractTrainingSession numRows(int numRows) {
        this.numRows = numRows;
        return this;
    }

    @Override
    public AbstractTrainingSession epochsTrained(int epochsTrained) {
        this.epochsTrained = epochsTrained;
        return this;
    }

    @Override
    public AbstractTrainingSession maxEpochs(int maxEpochs) {
        this.maxEpochs = maxEpochs;
        return this;
    }

    @Override
    public AbstractTrainingSession startingLoss(double startingLoss) {
        this.startingLoss = startingLoss;
        return this;
    }

    @Override
    public AbstractTrainingSession finalLoss(double finalLoss) {
        this.finalLoss = finalLoss;
        return this;
    }

    @Override
    public AbstractTrainingSession modelType(ModelType modelType) {
        this.modelType = modelType;
        return this;
    }

    @Override
    public AbstractTrainingSession modelType(String modelType) {
        this.modelType = ModelType.from(modelType);
        return this;
    }

    @Override
    public AbstractTrainingSession queryType(TrainingQueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    @Override
    public AbstractTrainingSession queryType(String queryType) {
        this.queryType = TrainingQueryType.from(queryType);
        return this;
    }

    @Override
    public AbstractTrainingSession trainingStartTime(LocalDateTime startTime) {
        this.trainingStartTime = startTime;
        return this;
    }

    @Override
    public AbstractTrainingSession trainingStartTime(String startTime) {
        this.trainingStartTime = LocalDateTime.parse(startTime);
        return this;
    }

    @Override
    public AbstractTrainingSession trainingEndTime(LocalDateTime endTime) {
        this.trainingEndTime = endTime;
        return this;
    }

    @Override
    public AbstractTrainingSession trainingEndTime(String endTime) {
        this.trainingEndTime = LocalDateTime.parse(endTime);
        return this;
    }

    @Override
    public AbstractTrainingSession queryStartTime(LocalDateTime startTime) {
        this.queryStartTime = startTime;
        return this;
    }

    @Override
    public AbstractTrainingSession queryStartTime(String startTime) {
        this.queryStartTime = LocalDateTime.parse(startTime);
        return this;
    }

    @Override
    public AbstractTrainingSession queryEndTime(LocalDateTime endTime) {
        this.queryEndTime = endTime;
        return this;
    }

    @Override
    public AbstractTrainingSession queryEndTime(String endTime) {
        this.queryEndTime = LocalDateTime.parse(endTime);
        return this;
    }

    @Override
    public AbstractTrainingSession sequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        return this;
    }

    @Override
    public AbstractTrainingSession batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public AbstractTrainingSession dimensionWidth(int dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    @Override
    public AbstractTrainingSession queryLoad(QueryLoad queryLoad) {
        this.queryLoad = queryLoad;
        return this;
    }

    @Override
    public AbstractTrainingSession queryLoad(String queryLoad) {
        this.queryLoad = QueryLoad.from(queryLoad);
        return this;
    }

    @Override
    public AbstractTrainingSession queryBatchSize(Integer queryBatchSize) {
        this.queryBatchSize = queryBatchSize;
        return this;
    }

    @Override
    public AbstractTrainingSession trainingDevice(TrainingDevice trainingDevice) {
        this.trainingDevice = trainingDevice;
        return this;
    }

    @Override
    public AbstractTrainingSession trainingDevice(String trainingDevice) {
        this.trainingDevice = TrainingDevice.from(trainingDevice);
        return this;
    }

    @Override
    public TrainingSession build() {
        return new TrainingSession(this.currency,
                                   this.prediction,
                                   this.numRows,
                                   this.epochsTrained,
                                   this.maxEpochs,
                                   this.startingLoss,
                                   this.finalLoss,
                                   this.modelType,
                                   this.queryType,
                                   this.trainingStartTime,
                                   this.trainingEndTime,
                                   this.queryStartTime,
                                   this.queryEndTime,
                                   this.sequenceLength,
                                   this.batchSize,
                                   this.dimensionWidth,
                                   this.queryLoad,
                                   this.queryBatchSize,
                                   this.trainingDevice
        );
    }
}
