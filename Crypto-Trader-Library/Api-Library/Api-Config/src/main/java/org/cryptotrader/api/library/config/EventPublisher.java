package org.cryptotrader.api.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@ConditionalOnBean(BindingService.class)
@ConditionalOnProperty(prefix = "spring.cloud.stream", name = "enabled", havingValue = "true", matchIfMissing = true)
@Component
public class EventPublisher {
    private final StreamBridge streamBridge;

    @Autowired
    public EventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public <T> void publish(String bindingName, T payload) {
        this.streamBridge.send(bindingName, payload);
    }
}
