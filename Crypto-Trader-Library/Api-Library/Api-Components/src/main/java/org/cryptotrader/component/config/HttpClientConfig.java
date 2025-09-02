package org.cryptotrader.component.config;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public HttpGet getHttpClient() {
        return new HttpGet();
    }

    @Bean
    public HttpPost postHttpClient() {
        return new HttpPost();
    }

    @Bean
    public HttpPut putHttpClient() {
        return new HttpPut();
    }

    @Bean
    public HttpDelete deleteHttpClient() {
        return new HttpDelete();
    }

    @Bean
    public HttpOptions optionsHttpClient() {
        return new HttpOptions();
    }
}

