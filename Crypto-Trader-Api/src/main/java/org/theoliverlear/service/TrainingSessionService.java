package org.theoliverlear.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theoliverlear.comm.request.TrainingSessionRequest;
import org.theoliverlear.entity.training.TrainingSession;
import org.theoliverlear.repository.TrainingSessionRepository;

@Service
@Slf4j
public class TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    
    @Autowired
    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }
    
    public void saveTrainingSession(TrainingSessionRequest request) {
        TrainingSession trainingSession = TrainingSession.fromRequest(request);
        this.trainingSessionRepository.save(trainingSession);
    }
}
