package com.data.synchronisation.springboot.config;


import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.data.synchronisation.springboot.service.OrderBookService;
import com.data.synchronisation.springboot.websocket.orderBook.client.BinanceWebSocketClient;

@Configuration
public class WebSocketStartup {

    @Bean
    public BinanceWebSocketClient binanceWebSocketClient(OrderBookService orderBookService) throws Exception {
        return new BinanceWebSocketClient(orderBookService);
    }

    @Bean
    public ApplicationRunner runWebSocket(BinanceWebSocketClient binanceWebSocketClient) {
        return args -> binanceWebSocketClient.connect();
    }
}