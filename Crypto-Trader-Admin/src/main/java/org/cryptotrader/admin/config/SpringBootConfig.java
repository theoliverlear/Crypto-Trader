package org.cryptotrader.admin.config;

import org.cryptotrader.desktop.component.config.SpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.cryptotrader.admin",
        "org.cryptotrader.api.services",
        "org.cryptotrader.component",
        "org.cryptotrader.desktop.component"})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.repository"
})
@EntityScan(basePackages = "org.cryptotrader.entity")
public class SpringBootConfig {

}
