package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.training.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    
}
