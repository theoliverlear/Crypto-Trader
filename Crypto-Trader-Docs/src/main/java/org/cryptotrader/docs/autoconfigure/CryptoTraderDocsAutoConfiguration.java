package org.cryptotrader.docs.autoconfigure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnProperty(prefix = "docs.autoconfigure", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CryptoTraderDocsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI cryptoTraderOpenAPI(Environment env) {
        String title = env.getProperty("docs.openapi.title", "Crypto Trader");
        String version = env.getProperty("docs.openapi.version", "v1");
        String description = env.getProperty("docs.openapi.description", "Algorithmic trader for cryptocurrencies.");
        String contactName = env.getProperty("docs.openapi.contact.name", "Sigwarth Software");
        String contactUrl = env.getProperty("docs.openapi.contact.url", "https://sigwarthsoftware.com");
        String serverUrl = env.getProperty("docs.openapi.server", "http://localhost:8080");

        Contact contact = new Contact();
        contact.setName(contactName);
        contact.setUrl(contactUrl);

        Info info = new Info()
                .title(title)
                .version(version)
                .description(description)
                .contact(contact);

        Server server = new Server().url(serverUrl);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
