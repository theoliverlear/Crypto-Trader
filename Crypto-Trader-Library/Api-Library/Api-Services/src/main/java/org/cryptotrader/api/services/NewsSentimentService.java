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
}
