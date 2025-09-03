package org.cryptotrader.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.cryptotrader.component.CurrencyJsonGenerator;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "org.cryptotrader.entity")
@ComponentScan(basePackages = {
        "org.cryptotrader.api"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = org.cryptotrader.component.config.HttpClientConfig.class))
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.repository"
})
public class CryptoTraderApiApplication {
    public static void main(String[] args) {
        // Detect 'docs' profile both from standard Spring property and from Spring Boot Maven plugin property
        String activeProfiles = System.getProperty("spring.profiles.active", "");
        String bootRunProfiles = System.getProperty("spring-boot.run.profiles", "");
        String combinedProfiles = (activeProfiles + "," + bootRunProfiles).toLowerCase();
        if (!combinedProfiles.contains("docs")) {
            // Only generate currencies outside of docs profile
            CurrencyJsonGenerator.standalone().generateAndSave();
        }
        SpringApplication.run(CryptoTraderApiApplication.class, args);
    }
}
