package org.cryptotrader.api.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;


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
