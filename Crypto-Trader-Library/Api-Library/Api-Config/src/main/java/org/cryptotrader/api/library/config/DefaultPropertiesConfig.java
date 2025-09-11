package org.cryptotrader.api.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("!test")
@PropertySource(value = "classpath:application-defaults.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class DefaultPropertiesConfig { }
