package org.cryptotrader.logging.config;

import org.cryptotrader.logging.CryptoTraderHttpLoggingProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@AutoConfiguration
@ConditionalOnClass(CommonsRequestLoggingFilter.class)
@ConditionalOnProperty(prefix = "cryptotrader.http.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HttpLoggingAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public CommonsRequestLoggingFilter requestLoggingFilter(CryptoTraderHttpLoggingProperties props) {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(props.isIncludeQueryString());
        filter.setIncludePayload(props.isIncludePayload());
        filter.setMaxPayloadLength(props.getMaxPayloadLength());
        filter.setIncludeHeaders(props.isIncludeHeaders());
        filter.setAfterMessagePrefix(props.getAfterMessagePrefix());
        return filter;
    }

    @Bean
    public CryptoTraderHttpLoggingProperties cryptoTraderHttpLoggingProperties() {
        return new CryptoTraderHttpLoggingProperties();
    }
}
