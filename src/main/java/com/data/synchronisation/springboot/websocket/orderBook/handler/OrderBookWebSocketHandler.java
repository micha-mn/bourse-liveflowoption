package com.data.synchronisation.springboot.websocket.orderBook.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderBookWebSocketHandler extends TextWebSocketHandler {
    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New client connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message from client: " + message.getPayload());
        // If clients send messages, you can handle them here (optional)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    public void broadcastOrderBook(String orderBookJson) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(orderBookJson));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}