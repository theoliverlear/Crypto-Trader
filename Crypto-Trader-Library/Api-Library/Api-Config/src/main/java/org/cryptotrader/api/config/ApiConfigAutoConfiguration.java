package org.cryptotrader.api.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({CorsConfig.class, HttpClientConfig.class, SecurityConfig.class})
public class ApiConfigAutoConfiguration {
}
