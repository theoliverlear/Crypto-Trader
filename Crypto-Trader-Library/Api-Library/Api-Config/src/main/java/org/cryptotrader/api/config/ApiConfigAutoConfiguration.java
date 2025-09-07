package org.cryptotrader.api.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource(value = "classpath:application-defaults.properties", ignoreResourceNotFound = true)
@Import({CorsConfig.class, HttpClientConfig.class, SecurityConfig.class})
public class ApiConfigAutoConfiguration {
}
