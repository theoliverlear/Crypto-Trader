package org.cryptotrader.entity.news.builder;

import org.cryptotrader.entity.news.NewsSentiment;
import org.cryptotrader.entity.news.builder.models.AbstractNewsSentiment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewsSentimentBuilder extends AbstractNewsSentiment {
    private Long articleId;
    private String title;
    private LocalDateTime publishedDate;
    private String source;
    private String url;
    private double positiveScore;
    private double neutralScore;
    private double negativeScore;
    private double compositeScore;
    private double cryptoRelevance;
    private LocalDateTime lastUpdated;
    
    public NewsSentimentBuilder() {
        this.articleId = 0L;
        this.title = "";
        this.publishedDate = null;
        this.source = "";
        this.url = "";
        this.positiveScore = 0;
        this.neutralScore = 0;
        this.negativeScore = 0;
        this.compositeScore = 0;
        this.cryptoRelevance = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public AbstractNewsSentiment articleId(Long articleId) {
        this.articleId = articleId;
        return this;
    }

    @Override
    public AbstractNewsSentiment title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public AbstractNewsSentiment publishedDate(String publishedDate) {
        this.publishedDate = LocalDateTime.parse(publishedDate,
                                                 DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return this;
    }

    @Override
    public AbstractNewsSentiment publishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    @Override
    public AbstractNewsSentiment source(String source) {
        this.source = source;
        return this;
    }

    @Override
    public AbstractNewsSentiment url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public AbstractNewsSentiment positiveScore(double positiveScore) {
        this.positiveScore = positiveScore;
        return this;
    }

    @Override
    public AbstractNewsSentiment neutralScore(double neutralScore) {
        this.neutralScore = neutralScore;
        return this;
    }

    @Override
    public AbstractNewsSentiment negativeScore(double negativeScore) {
        this.negativeScore = negativeScore;
        return this;
    }

    @Override
    public AbstractNewsSentiment compositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
        return this;
    }

    @Override
    public AbstractNewsSentiment cryptoRelevance(double cryptoRelevance) {
        this.cryptoRelevance = cryptoRelevance;
        return this;
    }

    @Override
    public AbstractNewsSentiment lastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public AbstractNewsSentiment lastUpdated(String lastUpdated) {
        this.lastUpdated = LocalDateTime.parse(lastUpdated);
        return this;
    }

    @Override
    public NewsSentiment build() {
        return new NewsSentiment(
                this.articleId,
                this.title,
                this.publishedDate,
                this.source,
                this.url,
                this.positiveScore,
                this.neutralScore,
                this.negativeScore,
                this.compositeScore,
                this.cryptoRelevance,
                this.lastUpdated
        );
    }
}
