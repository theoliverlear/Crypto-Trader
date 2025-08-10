package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class NewsSentimentRequest {
    private Long articleId;
    private String title;
    private String publishedDate;
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
                                String publishedDate,
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
        this.publishedDate = publishedDate;
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
