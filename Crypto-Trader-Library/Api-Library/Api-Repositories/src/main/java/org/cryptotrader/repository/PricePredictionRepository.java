package org.cryptotrader.repository;

import org.cryptotrader.entity.prediction.PricePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
}
