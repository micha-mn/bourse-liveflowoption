package com.data.synchronisation.springboot.dto.orderBook;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.data.synchronisation.springboot.dto.OrderBookByActionObjectProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class OrderBookBidAskListDTO {
	private List<OrderBookByActionObjectProjection> bid;
	private List<OrderBookByActionObjectProjection> ask;

}
