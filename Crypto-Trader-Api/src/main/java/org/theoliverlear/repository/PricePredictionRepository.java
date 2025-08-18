package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.prediction.PricePrediction;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
}
