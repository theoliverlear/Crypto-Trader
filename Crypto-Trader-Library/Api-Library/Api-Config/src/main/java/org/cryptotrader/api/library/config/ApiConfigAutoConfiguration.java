package org.cryptotrader.api.library.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        DefaultPropertiesConfig.class,
        ApplicationYamlConfig.class,
        ApplicationDocsYamlConfig.class,
        ApplicationTestYamlConfig.class,
        CorsConfig.class,
        HttpClientConfig.class,
        DocsSecurityConfig.class
})
public class ApiConfigAutoConfiguration {
}
