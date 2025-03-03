package com.data.synchronisation.springboot.websocket.orderBook.config;

import java.util.List;

import com.data.synchronisation.springboot.websocket.orderBook.dto.BinanceOrderBook;

public class DepthUpdateDTO {
    private String e; // Event type
    private long E;   // Event time
    private String s; // Symbol (e.g., BTCUSDT)
    private long U;   // First update ID in event
    private long u;   // Final update ID in event
    private List<BinanceOrderBook> b; // Bids (price levels to buy)
    private List<BinanceOrderBook> a; // Asks (price levels to sell)

    public DepthUpdateDTO() {}

    public DepthUpdateDTO(String e, long E, String s, long U, long u, List<BinanceOrderBook> b, List<BinanceOrderBook> a) {
        this.e = e;
        this.E = E;
        this.s = s;
        this.U = U;
        this.u = u;
        this.b = b;
        this.a = a;
    }

    public String getE() { return e; }
    public void setE(String e) { this.e = e; }

    public long getETime() { return E; }
    public void setETime(long E) { this.E = E; }

    public String getSymbol() { return s; }
    public void setSymbol(String s) { this.s = s; }

    public long getFirstUpdateId() { return U; }
    public void setFirstUpdateId(long U) { this.U = U; }

    public long getFinalUpdateId() { return u; }
    public void setFinalUpdateId(long u) { this.u = u; }

    public List<BinanceOrderBook> getBids() { return b; }
    public void setBids(List<BinanceOrderBook> b) { this.b = b; }

    public List<BinanceOrderBook> getAsks() { return a; }
    public void setAsks(List<BinanceOrderBook> a) { this.a = a; }

    @Override
    public String toString() {
        return "DepthUpdateDTO{" +
                "e='" + e + '\'' +
                ", E=" + E +
                ", s='" + s + '\'' +
                ", U=" + U +
                ", u=" + u +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}