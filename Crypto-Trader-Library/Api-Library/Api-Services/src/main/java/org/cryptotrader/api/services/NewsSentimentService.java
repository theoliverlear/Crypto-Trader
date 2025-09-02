package org.cryptotrader.api.services;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.comm.request.NewsSentimentRequest;
import org.cryptotrader.component.NewsSentimentHarvesterClient;
import org.cryptotrader.entity.news.NewsSentiment;
import org.cryptotrader.repository.NewsSentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class NewsSentimentService {
    private final NewsSentimentRepository newsSentimentRepository;
    private final NewsSentimentHarvesterClient sentimentHarvesterClient;

    @Autowired
    public NewsSentimentService(NewsSentimentRepository newsSentimentRepository,
                                NewsSentimentHarvesterClient sentimentHarvesterClient) {
        this.newsSentimentRepository = newsSentimentRepository;
        this.sentimentHarvesterClient = sentimentHarvesterClient;
    }

    private void handleOutdatedHarvest() {
        if (this.shouldHarvest()) {
            log.info("Harvest sentiments are out of date. Triggering a new harvest.");
            this.triggerDailySentimentHarvest();
        }
    }

    public void saveFromRequest(NewsSentimentRequest request) {
        log.info("Saving news sentiment from request: \"{}\"", request.getTitle());
        NewsSentiment newsSentiment = NewsSentiment.builder()
                                                   .articleId(request.getArticleId())
                                                   .title(request.getTitle())
                                                   .publishedDate(request.getPublishDate())
                                                   .source(request.getSource())
                                                   .url(request.getUrl())
                                                   .positiveScore(request.getPositiveScore())
                                                   .neutralScore(request.getNeutralScore())
                                                   .negativeScore(request.getNegativeScore())
                                                   .compositeScore(request.getCompositeScore())
                                                   .cryptoRelevance(request.getCryptoRelevance())
                                                   .build();
        log.info("{}", newsSentiment);
        if (!this.newsSentimentRepository.existsByArticleId(newsSentiment.getArticleId())) {
            this.newsSentimentRepository.save(newsSentiment);
        }
    }
    
    public boolean shouldHarvest() {
        LocalDateTime now = LocalDateTime.now();
        NewsSentiment lastInserted = this.newsSentimentRepository.getLastInserted();
        if (lastInserted == null) {
            return true;
        }
        LocalDateTime lastHarvest = lastInserted.getLastUpdated();
        return now.isAfter(lastHarvest.plusHours(1));
    }
    
    // TODO: Use event listeners to trigger these methods.
    
//    @Async
//    @Scheduled(fixedRate = 3600000)
    public void triggerDailySentimentHarvest() {
        log.info("Triggering hourly news sentiment harvest...");
        this.sentimentHarvesterClient.triggerHarvest();
    }

//    @Async
//    @Scheduled(fixedRate = 2592000000L)
    public void backFillDailySentimentHarvest() {
        log.info("Back-filling daily news sentiment harvest...");
        this.sentimentHarvesterClient.backFillDaily();
    }

//    @Async
//    @Scheduled(fixedRate = 604800000L)
    public void backFillWeeklySentimentHarvest() {
        log.info("Back-filling weekly news sentiment harvest...");
        this.sentimentHarvesterClient.backFillWeekly();
    }
}
