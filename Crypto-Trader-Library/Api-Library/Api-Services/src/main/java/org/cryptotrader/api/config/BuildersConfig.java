package org.cryptotrader.api.config;

import org.cryptotrader.api.services.PricePredictionService;
import org.cryptotrader.entity.prediction.PricePredictionLookup;
import org.cryptotrader.entity.training.builder.TrainingSessionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuildersConfig {

    @Bean
    @Primary
    public PricePredictionLookup pricePredictionLookup(PricePredictionService pricePredictionService) {
        return pricePredictionService::getById;
    }

    @Bean
    @Scope("prototype")
    public TrainingSessionBuilder trainingSessionBuilder(PricePredictionLookup predictionLookup) {
        return new TrainingSessionBuilder(predictionLookup);
    }
}
