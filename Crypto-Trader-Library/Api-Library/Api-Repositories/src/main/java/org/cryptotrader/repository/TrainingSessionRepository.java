package org.cryptotrader.repository;

import org.cryptotrader.entity.training.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    
}
