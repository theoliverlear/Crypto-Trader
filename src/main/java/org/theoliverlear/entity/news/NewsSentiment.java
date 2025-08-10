package org.theoliverlear.entity.news;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_sentiments")
@Getter
@Setter
public class NewsSentiment extends Identifiable {
    Long articleId;
    String title;
    LocalDateTime publishedDate;
    String source;
    String keywords;
    String url;
    double positiveScore;
    double neutralScore;
    double negativeScore;
    double compositeScore;
}
