package org.cryptotrader.data.controller;

import org.cryptotrader.api.library.services.PricePredictionService;
import org.cryptotrader.api.library.comm.request.PricePredictionRequest;
import org.cryptotrader.api.library.comm.response.PredictionIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/predictions")
public class PricePredictionController {
    private final PricePredictionService pricePredictionService;

    @Autowired
    public PricePredictionController(PricePredictionService pricePredictionService) {
        this.pricePredictionService = pricePredictionService;
    }
    @RequestMapping("/add")
    public ResponseEntity<PredictionIdResponse> predictions(@RequestBody PricePredictionRequest pricePredictionRequest) {
        if (pricePredictionRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long predictionId = this.pricePredictionService.savePrediction(pricePredictionRequest).getId();
        if (predictionId == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new PredictionIdResponse(predictionId), HttpStatus.ACCEPTED);
    }
}
