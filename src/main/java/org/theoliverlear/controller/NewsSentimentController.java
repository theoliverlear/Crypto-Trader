package org.theoliverlear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.NewsSentimentRequest;
import org.theoliverlear.comm.response.OperationSuccessfulResponse;
import org.theoliverlear.service.NewsSentimentService;

@RestController
@RequestMapping("/api/news-sentiment")
public class NewsSentimentController {
    private final NewsSentimentService newsSentimentService;
    
    @Autowired
    public NewsSentimentController(NewsSentimentService newsSentimentService) {
        this.newsSentimentService = newsSentimentService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<OperationSuccessfulResponse> add(@RequestBody NewsSentimentRequest request) {
        this.newsSentimentService.saveFromRequest(request);
        return new ResponseEntity<>(new OperationSuccessfulResponse(true), HttpStatus.OK);
    }
}
