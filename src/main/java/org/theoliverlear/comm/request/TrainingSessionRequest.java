package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class TrainingSessionRequest {
    private String currency;
    private Long prediction;
    private int numRows;
    private int epochsTrained;
    private int maxEpochs;
    private double startingLoss;
    private double finalLoss;
    private String modelType;
    private String queryType;
    private String trainingStartTime;
    private String trainingEndTime;
    private String queryStartTime;
    private String queryEndTime;
    private int sequenceLength;
    private int batchSize;
    private int dimensionWidth;
    private String queryLoad;
    private Integer queryBatchSize;
    private String trainingDevice;
    private Integer shortSequenceLength;
    private Integer mediumSequenceLength;
    private Integer longSequenceLength;
    
    public TrainingSessionRequest(String currency,
                                  Long prediction,
                                  int numRows,
                                  int epochsTrained,
                                  int maxEpochs,
                                  double startingLoss,
                                  double finalLoss,
                                  String modelType,
                                  String queryType,
                                  String trainingStartTime,
                                  String trainingEndTime,
                                  String queryStartTime,
                                  String queryEndTime,
                                  int sequenceLength,
                                  int batchSize,
                                  int dimensionWidth,
                                  String queryLoad,
                                  Integer queryBatchSize,
                                  String trainingDevice,
                                  Integer shortSequenceLength,
                                  Integer mediumSequenceLength,
                                  Integer longSequenceLength) {
        this.currency = currency;
        this.prediction = prediction;
        this.numRows = numRows;
        this.epochsTrained = epochsTrained;
        this.maxEpochs = maxEpochs;
        this.startingLoss = startingLoss;
        this.finalLoss = finalLoss;
        this.modelType = modelType;
        this.queryType = queryType;
        this.trainingStartTime = trainingStartTime;
        this.trainingEndTime = trainingEndTime;
        this.queryStartTime = queryStartTime;
        this.queryEndTime = queryEndTime;
        this.sequenceLength = sequenceLength;
        this.batchSize = batchSize;
        this.dimensionWidth = dimensionWidth;
        this.queryLoad = queryLoad;
        this.queryBatchSize = queryBatchSize;
        this.trainingDevice = trainingDevice;
        this.shortSequenceLength = shortSequenceLength;
        this.mediumSequenceLength = mediumSequenceLength;
        this.longSequenceLength = longSequenceLength;
    }
}
