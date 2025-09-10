package org.cryptotrader.api.library.services;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.comm.request.TrainingSessionRequest;
import org.cryptotrader.api.library.entity.training.TrainingSession;
import org.cryptotrader.api.library.entity.training.builder.TrainingSessionBuilder;
import org.cryptotrader.api.library.repository.TrainingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    private TrainingSessionBuilder trainingSessionBuilder;
    
    @Autowired
    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository,
                                  TrainingSessionBuilder trainingSessionBuilder) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.trainingSessionBuilder = trainingSessionBuilder;
    }
    
    public void saveTrainingSession(TrainingSessionRequest request) {
        TrainingSession trainingSession = this.fromRequest(request);
        this.trainingSessionRepository.save(trainingSession);
    }
    
    public TrainingSession fromRequest(TrainingSessionRequest request) {
        return this.trainingSessionBuilder
                .currency(request.getCurrency())
                .prediction(request.getPrediction())
                .numRows(request.getNumRows())
                .epochsTrained(request.getEpochsTrained())
                .maxEpochs(request.getMaxEpochs())
                .startingLoss(request.getStartingLoss())
                .finalLoss(request.getFinalLoss())
                .modelType(request.getModelType())
                .queryType(request.getQueryType())
                .trainingStartTime(request.getTrainingStartTime())
                .trainingEndTime(request.getTrainingEndTime())
                .queryStartTime(request.getQueryStartTime())
                .queryEndTime(request.getQueryEndTime())
                .sequenceLength(request.getSequenceLength())
                .batchSize(request.getBatchSize())
                .dimensionWidth(request.getDimensionWidth())
                .queryLoad(request.getQueryLoad())
                .queryBatchSize(request.getQueryBatchSize())
                .trainingDevice(request.getTrainingDevice())
                .shortSequenceLength(request.getShortSequenceLength())
                .mediumSequenceLength(request.getMediumSequenceLength())
                .longSequenceLength(request.getLongSequenceLength())
                .build();
    }
}
