package org.cryptotrader.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
//        return factory -> {
//            Connector httpConnector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//            httpConnector.setScheme("http");
//            httpConnector.setPort(8080);
//            httpConnector.setSecure(false);
//            factory.addAdditionalTomcatConnectors(httpConnector);
//        };
//    }
}
