package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.theoliverlear.entity.news.NewsSentiment;

public interface NewsSentimentRepository extends JpaRepository<NewsSentiment, Long> {
    
    @Query("SELECT n FROM NewsSentiment n ORDER BY n.id DESC")
    NewsSentiment getLastInserted();
}
