package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.training.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    
}
