package org.cryptotrader.api.controller.websocket;
//=================================-Imports-==================================
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/trader")
public class TraderWebSocketController {
    //=============================-Methods-==================================

    //------------------------------On-Open-----------------------------------
    public void onOpen(Session session) {

    }
}