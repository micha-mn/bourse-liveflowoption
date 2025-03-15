package com.data.synchronisation.springboot.config;


import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.data.synchronisation.springboot.service.OrderBookService;
import com.data.synchronisation.springboot.websocket.orderBook.client.BinanceWebSocketClient;
import com.data.synchronisation.springboot.websocket.orderBook.client.TestWebSocket;

@Configuration
public class WebSocketStartup {

	/*
    @Bean
    public BinanceWebSocketClient binanceWebSocketClient(OrderBookService orderBookService) throws Exception {
        return new BinanceWebSocketClient(orderBookService);
    }

    @Bean
    public ApplicationRunner runWebSocket(BinanceWebSocketClient binanceWebSocketClient) {
        return args -> binanceWebSocketClient.connect();
    }
    */
    
	
	 @Bean
	    public TestWebSocket binanceWebSocketClient(OrderBookService orderBookService) throws Exception {
	        return new TestWebSocket(orderBookService);
	    }

	    @Bean
	    public ApplicationRunner runWebSocket(TestWebSocket testWebSocket) {
	        return args -> testWebSocket.connect();
	    }
	    
	    
    
}