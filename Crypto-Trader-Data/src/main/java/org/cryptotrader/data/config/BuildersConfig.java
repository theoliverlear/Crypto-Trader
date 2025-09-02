package org.cryptotrader.data.config;

import org.cryptotrader.entity.prediction.PricePredictionLookup;
import org.cryptotrader.entity.training.builder.TrainingSessionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuildersConfig {
    @Bean
    @Scope("prototype")
    public TrainingSessionBuilder trainingSessionBuilder(PricePredictionLookup predictionLookup) {
        return new TrainingSessionBuilder(predictionLookup);
    }
}
