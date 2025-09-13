package org.cryptotrader.admin.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.cryptotrader.admin",
        "org.cryptotrader.api.library.services",
        "org.cryptotrader.api.library.component",
        "org.cryptotrader.desktop.library.component"})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.api.library.repository"
})
@EntityScan(basePackages = "org.cryptotrader.entity")
public class SpringBootConfig {

}
