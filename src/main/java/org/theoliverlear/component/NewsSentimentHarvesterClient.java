package org.theoliverlear.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.theoliverlear.comm.request.NewsSentimentHarvestRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@Component
@Slf4j
public class NewsSentimentHarvesterClient {
    private static final String API_URL = "http://localhost:8000/api/news-sentiment/harvest";
    private static final NewsSentimentHarvestRequest DEFAULT_REQUEST = new NewsSentimentHarvestRequest(50, 0, 1);
    private final HttpPost httpPost;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;
    
    @Autowired
    public NewsSentimentHarvesterClient(HttpPost httpPost,
                                        ObjectMapper objectMapper,
                                        CloseableHttpClient httpClient) {
        this.httpPost = httpPost;
        this.httpPost.setURI(URI.create(API_URL));
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }
    
    public void triggerHarvest() {
        this.triggerHarvest(DEFAULT_REQUEST);
    }
    
    public String requestToJson(NewsSentimentHarvestRequest request) {
        try {
            return this.objectMapper.writeValueAsString(request);
        } catch (Exception ex) {
            log.error("Error converting request to JSON.", ex);
            return null;
        }
    }
    
    public void triggerHarvest(NewsSentimentHarvestRequest request) {
        log.info("Sending harvest request...");
        String json = this.requestToJson(request);
        try {
            this.httpPost.setEntity(new StringEntity(json));
            this.httpPost.setHeader("Content-Type", "application/json");
            this.httpPost.setHeader("Accept", "application/json");
            this.httpPost.setHeader("Accept-Charset", "UTF-8");
            CloseableHttpResponse response = this.httpClient.execute(this.httpPost);
            log.info("Harvest request sent. Response status: {}", response.getStatusLine().getStatusCode());
        } catch (IOException exception) {
            log.error("Failed to set request entity.", exception);
        }
    }
}
