package org.cryptotrader.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.training.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    
}
