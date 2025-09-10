package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.news.NewsSentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsSentimentRepository extends JpaRepository<NewsSentiment, Long> {
    
    @Query("SELECT n FROM NewsSentiment n ORDER BY n.id DESC")
    NewsSentiment getLastInserted();
    boolean existsByArticleId(Long newsId);
}
