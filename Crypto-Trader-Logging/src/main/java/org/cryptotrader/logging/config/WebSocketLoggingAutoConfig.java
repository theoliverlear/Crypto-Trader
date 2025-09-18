package org.cryptotrader.logging.config;

import org.cryptotrader.logging.properties.CryptoTraderWebSocketLoggingProperties;
import org.cryptotrader.logging.websocket.StompChannelLoggingInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@AutoConfiguration
@ConditionalOnClass(WebSocketMessageBrokerConfigurer.class)
@ConditionalOnProperty(prefix = "cryptotrader.websocket.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebSocketLoggingAutoConfig implements WebSocketMessageBrokerConfigurer {

    @org.springframework.beans.factory.annotation.Autowired
    private ChannelInterceptor interceptor;

    @Bean
    @ConditionalOnMissingBean
    public CryptoTraderWebSocketLoggingProperties cryptoTraderWebSocketLoggingProperties() {
        return new CryptoTraderWebSocketLoggingProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ChannelInterceptor.class)
    public ChannelInterceptor stompChannelLoggingInterceptor(CryptoTraderWebSocketLoggingProperties props) {
        return new StompChannelLoggingInterceptor(props);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptor);
    }
}
