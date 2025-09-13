package org.cryptotrader.api.library.communication.response;

import lombok.Data;

@Data
public class PredictionIdResponse {
    private Long predictionId;
    public PredictionIdResponse(Long predictionId) {
        this.predictionId = predictionId;
    }
}