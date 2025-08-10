package org.theoliverlear.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.comm.request.NewsSentimentRequest;
import org.theoliverlear.component.NewsSentimentHarvesterClient;
import org.theoliverlear.entity.news.NewsSentiment;
import org.theoliverlear.repository.NewsSentimentRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NewsSentimentService {
    private final NewsSentimentRepository newsSentimentRepository;
    private final NewsSentimentHarvesterClient sentimentHarvesterClient;

    @Autowired
    public NewsSentimentService(NewsSentimentRepository newsSentimentRepository,
                                NewsSentimentHarvesterClient sentimentHarvesterClient) {
        this.newsSentimentRepository = newsSentimentRepository;
        this.sentimentHarvesterClient = sentimentHarvesterClient;
        this.handleOutdatedHarvest();
    }

    private void handleOutdatedHarvest() {
        if (this.shouldHarvest()) {
            log.info("Harvest sentiments are out of date. Triggering a new harvest.");
            this.triggerHarvest();
        }
    }

    public void saveFromRequest(NewsSentimentRequest request) {
        log.info("Saving news sentiment from request: \"{}\"", request.getTitle());
        NewsSentiment newsSentiment = NewsSentiment.builder()
                                                   .articleId(request.getArticleId())
                                                   .title(request.getTitle())
                                                   .publishedDate(request.getPublishedDate())
                                                   .source(request.getSource())
                                                   .url(request.getUrl())
                                                   .positiveScore(request.getPositiveScore())
                                                   .neutralScore(request.getNeutralScore())
                                                   .negativeScore(request.getNegativeScore())
                                                   .compositeScore(request.getCompositeScore())
                                                   .cryptoRelevance(request.getCryptoRelevance())
                                                   .build();
        this.newsSentimentRepository.save(newsSentiment);
    }
    
    public boolean shouldHarvest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastHarvest = this.newsSentimentRepository.getLastInserted().getLastUpdated();
        return now.isAfter(lastHarvest.plusHours(1));
    }
    
    @Async
    @Scheduled(fixedRate = 3600000)
    public void triggerHarvest() {
        log.info("Triggering hourly news sentiment harvest...");
        this.sentimentHarvesterClient.triggerHarvest();
    }
}
