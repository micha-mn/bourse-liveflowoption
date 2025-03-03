package com.data.synchronisation.springboot.websocket.orderBook.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.data.synchronisation.springboot.websocket.orderBook.handler.OrderBookWebSocketHandler;

import org.springframework.context.annotation.Bean;

//@Configuration
// @EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final OrderBookWebSocketHandler orderBookWebSocketHandler;

    public WebSocketConfig(OrderBookWebSocketHandler orderBookWebSocketHandler) {
        this.orderBookWebSocketHandler = orderBookWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderBookWebSocketHandler, "/orderbook").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(512 * 1024); // 512 KB
        container.setMaxBinaryMessageBufferSize(512 * 1024); // 512 KB
        return container;
    }
}