package org.cryptotrader.api.library.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        DefaultPropertiesConfig.class,
        DocsSecurityConfig.class,
        ApplicationYamlConfig.class,
        ApplicationTestYamlConfig.class,
        CorsConfig.class,
        HttpClientConfig.class,
        SecurityConfig.class,
        EventPublisher.class
})
public class ApiConfigAutoConfiguration {
}
