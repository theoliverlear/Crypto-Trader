package org.cryptotrader.logging.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "cryptotrader.websocket.logging")
public class CryptoTraderWebSocketLoggingProperties {
    private boolean enabled = true;
    private boolean includeHeaders = false;
    private boolean includePayload = false;
    private int maxPayloadLength = 512;
    private boolean colorEnabled = true;
}
