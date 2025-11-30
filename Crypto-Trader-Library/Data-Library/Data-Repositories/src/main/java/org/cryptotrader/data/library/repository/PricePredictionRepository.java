package org.cryptotrader.data.library.repository;

import org.cryptotrader.data.library.entity.prediction.PricePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
}
