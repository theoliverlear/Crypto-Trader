package org.theoliverlear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.PricePredictionRequest;
import org.theoliverlear.comm.response.OperationSuccessfulResponse;
import org.theoliverlear.comm.response.PredictionIdResponse;
import org.theoliverlear.model.http.PayloadStatusResponse;
import org.theoliverlear.service.PricePredictionService;

@RestController
@RequestMapping("/api/predictions")
public class PricePredictionController {
    private PricePredictionService pricePredictionService;
    @Autowired
    public PricePredictionController(PricePredictionService pricePredictionService) {
        this.pricePredictionService = pricePredictionService;
    }
    @RequestMapping("/add")
    public ResponseEntity<PredictionIdResponse> predictions(@RequestBody PricePredictionRequest pricePredictionRequest) {
        Long predictionId = this.pricePredictionService.savePrediction(pricePredictionRequest).getId();
        return new ResponseEntity<>(new PredictionIdResponse(predictionId), HttpStatus.ACCEPTED);
    }
}
