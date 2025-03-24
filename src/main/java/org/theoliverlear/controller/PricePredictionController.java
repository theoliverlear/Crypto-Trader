package org.theoliverlear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.PricePredictionRequest;
import org.theoliverlear.comm.response.OperationSuccessfulResponse;
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
    @RequestMapping("")
    public ResponseEntity<PayloadStatusResponse<OperationSuccessfulResponse>> predictions(@RequestBody PricePredictionRequest pricePredictionRequest) {
        this.pricePredictionService.savePrediction(pricePredictionRequest);
        return new ResponseEntity<>(new PayloadStatusResponse<>(new OperationSuccessfulResponse(true), HttpStatus.ACCEPTED), HttpStatus.ACCEPTED);
    }
}
