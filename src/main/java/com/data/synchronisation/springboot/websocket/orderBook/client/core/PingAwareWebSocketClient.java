package com.data.synchronisation.springboot.websocket.orderBook.client.core;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.Opcode;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PongFrame;
import org.java_websocket.WebSocket;

import java.net.URI;
import java.nio.ByteBuffer;

public abstract class PingAwareWebSocketClient extends WebSocketClient {

	  public PingAwareWebSocketClient(URI serverUri, Draft draft) {
	        super(serverUri, draft);
	    }


    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        try {
            if (f.getOpcode() == Opcode.PING) {
                ByteBuffer payload = f.getPayloadData();
                PongFrame pong = new PongFrame();
                pong.setPayload(payload);
                conn.sendFrame(pong);
                System.out.println("📡 [PingAwareWebSocketClient] Sent PONG in response to Binance PING");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to send pong: " + e.getMessage());
        }
    }
}
