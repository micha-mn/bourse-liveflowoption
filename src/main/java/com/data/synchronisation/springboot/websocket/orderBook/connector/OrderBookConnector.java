package com.data.synchronisation.springboot.websocket.orderBook.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.data.synchronisation.springboot.websocket.orderBook.client.BinanceOrderBookClient;

// @Component
public class OrderBookConnector {
	// enable weboskcet to make binance trigger live updates to our servcer 
	
	private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/btcusdt@depth";
	@Bean
	public void binanceOrderBookWebsocket() {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			container.connectToServer(BinanceOrderBookClient.class, new URI(BINANCE_WS_URL));
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException | DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
