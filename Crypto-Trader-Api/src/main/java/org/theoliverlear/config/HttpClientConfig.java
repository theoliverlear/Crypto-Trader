package org.theoliverlear.config;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
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
