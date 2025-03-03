package com.data.synchronisation.springboot.websocket.orderBook.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceOrderBook {
	    private BigDecimal price;
	    private BigDecimal quantity;

	    public BinanceOrderBook() {}

	    public BinanceOrderBook(BigDecimal price, BigDecimal quantity) {
	        this.price = price;
	        this.quantity = quantity;
	    }

	    public BigDecimal getPrice() {
	        return price;
	    }

	    public BigDecimal getQuantity() {
	        return quantity;
	    }
	    
	    @Override
	    public String toString() {
	        return "Order{" +
	                "price=" + price +
	                ", quantity=" + quantity +
	                '}';
	    }
	    
	    
	    public static List<CRBTCOrderBook> convertToEntity(List<BinanceOrderBook> binanceOrderBook, String sellOrBuy /*sell or buy */, Long timeStamp) {
	    	
	    	List<CRBTCOrderBook> crBTCOrderBookLst = new ArrayList<>();
	    	binanceOrderBook.stream()
	        .forEach(orderBook -> 
	                {
	               crBTCOrderBookLst.add(CRBTCOrderBook.builder()
		                	.orderTimestamp(timeStamp)
			        		.quantity(orderBook.getQuantity())
			        		.value(orderBook.getPrice())
			        		.referDate(LocalDateTime.now())
			        		.action(sellOrBuy)
			        		.build()
			        		);
	                });
	    	return crBTCOrderBookLst;
	    	
	    }
	    
}
