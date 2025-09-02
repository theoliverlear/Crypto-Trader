package org.cryptotrader.data.controller;

import org.cryptotrader.api.services.TrainingSessionService;
import org.cryptotrader.comm.request.TrainingSessionRequest;
import org.cryptotrader.comm.response.OperationSuccessfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/training-session")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @Autowired
    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }
    
    
    @RequestMapping("/add")
    public ResponseEntity<OperationSuccessfulResponse> addTrainingSession(@RequestBody TrainingSessionRequest request) {
        this.trainingSessionService.saveTrainingSession(request);
        return new ResponseEntity<>(new OperationSuccessfulResponse(true), HttpStatus.OK);
    }
}
