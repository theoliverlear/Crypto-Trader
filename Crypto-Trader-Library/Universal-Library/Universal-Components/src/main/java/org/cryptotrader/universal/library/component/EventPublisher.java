package org.cryptotrader.universal.library.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    public <T> void publish(String bindingName, T payload, Map<String, Object> headers) {
        Message<T> message = MessageBuilder
                .withPayload(payload)
                .copyHeaders(headers)
                .build();

        this.streamBridge.send(bindingName, message);
    }
}
