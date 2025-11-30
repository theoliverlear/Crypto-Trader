package org.cryptotrader.data.library.repository;

import org.cryptotrader.data.library.entity.training.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    
}
