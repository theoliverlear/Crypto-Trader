package org.cryptotrader.api.controller;

import org.cryptotrader.comm.request.AssetValueRequest;
import org.cryptotrader.comm.response.AssetValueResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight docs-only controller so the app can start under the "docs" profile
 * without loading heavy service beans. It mirrors the endpoint signature so
 * SpringDoc can generate the OpenAPI description.
 */
@RestController
@Profile("docs")
@RequestMapping("/api/currency")
public class CurrencyDocsController {

    @PostMapping("/value")
    public ResponseEntity<AssetValueResponse> getCurrencyValue(@RequestBody AssetValueRequest assetValueRequest) {
        // Simple deterministic stub so the endpoint shape exists; no DB calls.
        double price = 1.23; // arbitrary constant for docs
        double assetValue = price * assetValueRequest.getShares();
        return new ResponseEntity<>(new AssetValueResponse(assetValue), HttpStatus.OK);
    }
}
