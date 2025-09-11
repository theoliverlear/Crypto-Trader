package org.cryptotrader.api.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Profile;

/**
 * Load application.yml via @PropertySource for non-docs profiles only.
 * In docs profile, let Spring Boot's profile-specific configuration
 * (application-docs.properties/yml) take precedence without being overridden.
 */
@Profile("!docs")
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationYamlConfig { }
