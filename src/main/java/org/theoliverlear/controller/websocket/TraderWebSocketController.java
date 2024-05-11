package org.theoliverlear.controller.websocket;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/trader")
public class TraderWebSocketController {
    public void onOpen(Session session) {

    }
}