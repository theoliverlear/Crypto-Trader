package org.cryptotrader.api.library.component.config;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(value = "apiComponentsHttpClientConfig", proxyBeanMethods = false)
public class HttpClientConfig {
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean(name = "apacheHttpClient4")
    @ConditionalOnMissingBean(CloseableHttpClient.class)
    public CloseableHttpClient httpClient4() {
        return HttpClients.createDefault();
    }

    @Bean
    @ConditionalOnMissingBean(HttpGet.class)
    public HttpGet getHttpClient() {
        return new HttpGet();
    }

    @Bean
    @ConditionalOnMissingBean(HttpPost.class)
    public HttpPost postHttpClient() {
        return new HttpPost();
    }

    @Bean
    @ConditionalOnMissingBean(HttpPut.class)
    public HttpPut putHttpClient() {
        return new HttpPut();
    }

    @Bean
    @ConditionalOnMissingBean(HttpDelete.class)
    public HttpDelete deleteHttpClient() {
        return new HttpDelete();
    }

    @Bean
    @ConditionalOnMissingBean(HttpOptions.class)
    public HttpOptions optionsHttpClient() {
        return new HttpOptions();
    }
}

