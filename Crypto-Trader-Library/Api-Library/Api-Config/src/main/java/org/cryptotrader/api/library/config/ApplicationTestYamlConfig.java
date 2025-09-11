package org.cryptotrader.api.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("test")
@PropertySource(value = "classpath:application-test.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationTestYamlConfig { }
