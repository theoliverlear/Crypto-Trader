package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.prediction.PricePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
}
