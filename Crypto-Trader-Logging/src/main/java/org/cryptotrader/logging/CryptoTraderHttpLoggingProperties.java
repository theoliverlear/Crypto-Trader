package org.cryptotrader.logging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "cryptotrader.http.logging")
public class CryptoTraderHttpLoggingProperties {
    private boolean enabled = true;
    private boolean includeQueryString = true;
    private boolean includePayload = true;
    private int maxPayloadLength = 512;
    private boolean includeHeaders = false;
    private String afterMessagePrefix = "REQUEST -> ";
}
