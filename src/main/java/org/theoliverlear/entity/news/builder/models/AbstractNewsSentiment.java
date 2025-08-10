package org.theoliverlear.entity.news.builder.models;

import org.theoliverlear.entity.news.NewsSentiment;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractNewsSentiment implements BuilderFactory<NewsSentiment> {
    public abstract AbstractNewsSentiment articleId(Long articleId);
    public abstract AbstractNewsSentiment title(String title);
    public abstract AbstractNewsSentiment publishedDate(String publishedDate);
    public abstract AbstractNewsSentiment publishedDate(LocalDateTime publishedDate);
    public abstract AbstractNewsSentiment source(String source);
    public abstract AbstractNewsSentiment url(String url);
    public abstract AbstractNewsSentiment positiveScore(double positiveScore);
    public abstract AbstractNewsSentiment neutralScore(double neutralScore);
    public abstract AbstractNewsSentiment negativeScore(double negativeScore);
    public abstract AbstractNewsSentiment compositeScore(double compositeScore);
    public abstract AbstractNewsSentiment cryptoRelevance(double cryptoRelevance);
    public abstract AbstractNewsSentiment lastUpdated(LocalDateTime lastUpdated);
    public abstract AbstractNewsSentiment lastUpdated(String lastUpdated);
}