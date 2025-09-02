package org.cryptotrader.data.controller;

import org.cryptotrader.api.services.PricePredictionService;
import org.cryptotrader.comm.request.PricePredictionRequest;
import org.cryptotrader.comm.response.PredictionIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/predictions")
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
