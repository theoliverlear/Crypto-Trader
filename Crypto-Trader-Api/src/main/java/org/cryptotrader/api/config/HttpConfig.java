package org.cryptotrader.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(512);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST -> ");
        return filter;
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
