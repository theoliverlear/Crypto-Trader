package org.cryptotrader.api.library.services;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.comm.request.NewsSentimentRequest;
import org.cryptotrader.api.library.component.NewsSentimentHarvesterClient;
import org.cryptotrader.api.library.entity.news.NewsSentiment;
import org.cryptotrader.api.library.repository.NewsSentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
