package com.data.synchronisation.springboot.websocket.orderBook.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.data.synchronisation.springboot.service.OrderBookService;
import com.data.synchronisation.springboot.websocket.orderBook.config.DepthUpdateDTO;
import com.data.synchronisation.springboot.websocket.orderBook.dto.BinanceOrderBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class BinanceOrderBookClient {
	

    private static final List<BinanceOrderBook> buyOrders = new CopyOnWriteArrayList<>();
    private static final List<BinanceOrderBook> sellOrders = new CopyOnWriteArrayList<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Session session;
    private StringBuilder messageBuffer = new StringBuilder();  // Buffer to store partial messages
    
    
    @Autowired
    OrderBookService orderBookService;
    
    public BinanceOrderBookClient() {
        // Enable Spring to inject dependencies into this WebSocket endpoint
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to Binance WebSocket Order Book.");
    }

    @OnMessage
    public void onMessage(String message, boolean isLastPart) {
    	messageBuffer.append(message);  // Append partial message

        if (isLastPart) {  // Process only when full message is received
            processFullMessage(messageBuffer.toString());
            messageBuffer.setLength(0);  // Clear buffer
        }
        
        /*
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            List<BinanceOrderBook> newBuyOrders = parseOrders(jsonNode.get("bids"),10);
            List<BinanceOrderBook> newSellOrders = parseOrders(jsonNode.get("asks"),10);

            buyOrders.clear();
            buyOrders.addAll(newBuyOrders);

            sellOrders.clear();
            sellOrders.addAll(newSellOrders);

            System.out.println("Updated Order Book: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
    
    @Async
    private void processFullMessage(String fullMessage) {
        try {
            System.out.println("c" + fullMessage);
            // You can parse JSON and filter large data here
            
            JsonNode jsonNode = objectMapper.readTree(fullMessage);

            // Extract event fields
            String eventType = jsonNode.get("e").asText();
            long eventTime = jsonNode.get("E").asLong();
            String symbol = jsonNode.get("s").asText();
            long firstUpdateId = jsonNode.get("U").asLong();
            long finalUpdateId = jsonNode.get("u").asLong();

            // Convert JSON "b" (bids) and "a" (asks) into Java lists
            List<BinanceOrderBook> bids = parseOrders(jsonNode.get("b"), 10); // Limit to top 10    //buy
            List<BinanceOrderBook> asks = parseOrders(jsonNode.get("a"), 10);  //sell

            // Create DTO
            DepthUpdateDTO depthUpdateDTO = new DepthUpdateDTO(eventType, eventTime, symbol, firstUpdateId, finalUpdateId, bids, asks);
            System.out.println("Converted Order Book DTO: " + depthUpdateDTO);
            
          //  orderBookService.saveOrderBookLst(BinanceOrderBook.convertToEntity(asks, "sell", eventTime));
          //  orderBookService.saveOrderBookLst(BinanceOrderBook.convertToEntity(bids, "buy", eventTime));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket Closed: " + reason);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    
    private List<BinanceOrderBook> parseOrders(JsonNode ordersNode, int limit) {
        List<BinanceOrderBook> orders = new ArrayList<>();

        if (ordersNode == null || ordersNode.isEmpty()) {
            System.out.println("Warning: Received empty orders.");
            return orders; // Return empty list to prevent errors
        }

        for (int i = 0; i < Math.min(limit, ordersNode.size()); i++) {
            BigDecimal price = new BigDecimal(ordersNode.get(i).get(0).asText());
            BigDecimal quantity = new BigDecimal(ordersNode.get(i).get(1).asText());
            orders.add(new BinanceOrderBook(price, quantity));
        }
        return orders;
    }
    
}
