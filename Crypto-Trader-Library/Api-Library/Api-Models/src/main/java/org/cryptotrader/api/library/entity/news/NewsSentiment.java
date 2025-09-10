package org.cryptotrader.api.library.entity.news;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.api.library.entity.Identifiable;
import org.cryptotrader.api.library.entity.news.builder.NewsSentimentBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "news_sentiments")
public class NewsSentiment extends Identifiable {
    @Column(name = "article_id", unique = true)
    private Long articleId;
    @Column(name = "article_title")
    private String title;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "source")
    private String source;
    @Column(name = "url")
    private String url;
    @Column(name = "positive_score")
    private double positiveScore;
    @Column(name = "neutral_score")
    private double neutralScore;
    @Column(name = "negative_score")
    private double negativeScore;
    @Column(name = "composite_score")
    private double compositeScore;
    @Column(name = "crypto_relevance")
    private double cryptoRelevance;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    @Column(name = "weighted_score")
    private double weightedScore;
    
    public NewsSentiment() {
        super();
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
        this.weightedScore = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public NewsSentiment(Long articleId,
                         String title,
                         LocalDateTime publishedDate,
                         String source,
                         String url,
                         double positiveScore,
                         double neutralScore,
                         double negativeScore,
                         double compositeScore,
                         double cryptoRelevance,
                         LocalDateTime lastUpdated) {
        super();
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
        this.weightedScore = calculateWeightedScore(compositeScore, cryptoRelevance);

    }
    
    public static NewsSentimentBuilder builder() {
        return new NewsSentimentBuilder();
    }
    
    public static double calculateWeightedScore(double compositeScore, double cryptoRelevance) {
        return compositeScore * cryptoRelevance;
    }
}


