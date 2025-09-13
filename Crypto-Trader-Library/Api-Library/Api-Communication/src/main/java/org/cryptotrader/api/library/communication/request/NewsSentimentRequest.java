package org.cryptotrader.api.library.communication.request;

import lombok.Data;

@Data
public class NewsSentimentRequest {
    private Long articleId;
    private String title;
    private String publishDate;
    private String source;
    private String url;
    private double positiveScore;
    private double neutralScore;
    private double negativeScore;
    private double compositeScore;
    private double cryptoRelevance;
    private String lastUpdated;
    public NewsSentimentRequest(Long articleId,
                                String title,
                                String publishDate,
                                String source,
                                String url,
                                double positiveScore,
                                double neutralScore,
                                double negativeScore,
                                double compositeScore,
                                double cryptoRelevance,
                                String lastUpdated) {
        this.articleId = articleId;
        this.title = title;
        this.publishDate = publishDate;
        this.source = source;
        this.url = url;
        this.positiveScore = positiveScore;
        this.neutralScore = neutralScore;
        this.negativeScore = negativeScore;
        this.compositeScore = compositeScore;
        this.cryptoRelevance = cryptoRelevance;
        this.lastUpdated = lastUpdated;
    }
}
