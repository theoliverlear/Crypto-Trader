package org.theoliverlear.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

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
