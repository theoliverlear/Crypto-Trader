package org.cryptotrader.api.library.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource(value = "classpath:application-defaults.properties", ignoreResourceNotFound = true)
@Import({ApplicationYamlConfig.class, CorsConfig.class, HttpClientConfig.class, SecurityConfig.class, EventPublisher.class, YamlPropertySourceFactory.class})
public class ApiConfigAutoConfiguration {
}
