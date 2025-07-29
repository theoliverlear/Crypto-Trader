package org.theoliverlear.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.theoliverlear.service.CurrencyService;

@Configuration
public class BackfillerConfig {
    //==============================-Beans-===================================

    //------------------------Snapshots-Cli-Runner----------------------------
    @Bean
    CommandLineRunner snapshotsCliRunner(CurrencyService currencyService,
                                         ApplicationArguments args) {
        return commandLineArgs -> {
            if (args.containsOption("buildSnapshots")) {
                boolean fullRefresh = args.containsOption("fullRefresh");
                currencyService.buildMarketSnapshots(fullRefresh);
            }
        };
    }
}