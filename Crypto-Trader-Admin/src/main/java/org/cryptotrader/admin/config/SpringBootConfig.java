package org.cryptotrader.admin.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.client.HttpClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {HttpClientAutoConfiguration.class, RestClientAutoConfiguration.class, WebServiceTemplateAutoConfiguration.class})
@ComponentScan(basePackages = {"org.cryptotrader.admin",
        "org.cryptotrader.api.library.services",
        "org.cryptotrader.api.library.component",
        "org.cryptotrader.api.library.events",
        "org.cryptotrader.api.library.config",
        "org.cryptotrader.desktop.library.component"})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.api.library.repository"
})
@EntityScan(basePackages = "org.cryptotrader.api.library.entity")
public class SpringBootConfig {

}
