package com.data.synchronisation.springboot.websocket.historicalTrades.dto;

import java.math.BigDecimal;

public class TradeHistoryMessage {
    private String tradeId;
    private String symbol;
    private BigDecimal price;
    private BigDecimal quantity;
    private long timestamp;

    public TradeHistoryMessage() {
    }

    public TradeHistoryMessage(String tradeId, String symbol, BigDecimal price, BigDecimal quantity, long timestamp) {
        this.tradeId = tradeId;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}