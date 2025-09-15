package org.cryptotrader.api.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Loads application-docs.yml from this library when the "docs" Spring profile is active.
 * This makes the CI docs workflow (which runs services with -Dspring-boot.run.profiles=docs)
 * pick up H2 and other relaxed settings, even if the consuming application does not ship
 * its own application-docs.yml.
 */
@Configuration
@Profile("docs")
@PropertySource(value = "classpath:application-docs.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationDocsYamlConfig { }
