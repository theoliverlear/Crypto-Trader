package org.cryptotrader.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.prediction.PricePrediction;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
}
