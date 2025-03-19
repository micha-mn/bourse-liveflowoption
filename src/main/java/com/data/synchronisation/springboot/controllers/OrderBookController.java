package com.data.synchronisation.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.synchronisation.springboot.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.dto.OrderBookResponseDTO;
import com.data.synchronisation.springboot.dto.orderBook.OrderBookDataDTO;
import com.data.synchronisation.springboot.service.OrderBookService;


@RestController
@RequestMapping(value="order-book")
public class OrderBookController {

	@Autowired
	private final OrderBookService orderBookService;
	

	public OrderBookController(OrderBookService orderBookService) {
		this.orderBookService = orderBookService;
	}

	//GraphGeneralResponseDTO
	@GetMapping(value = "/get-order-book-percentage")
	public List<GraphResponseProjection> getOrderBookPercentage(@RequestBody GraphDataReqDTO req) {
		// return null;
		 return orderBookService.getOrderBookPercentage(req);
	}
	
	
	@GetMapping(value = "/get-order-book-bid-ask")
	public OrderBookResponseDTO getOrderBookByBidAsk(@RequestBody GraphDataReqDTO req) {
		// return null;
		 return orderBookService.getOrderBookByBidAsk(req);
	}
	
	@PostMapping(value = "/get-order-book-data")
	public OrderBookDataDTO getOrderBookData(@RequestBody GraphDataReqDTO req) {
		// return null;
		 return orderBookService.getOrderBookData(req);
	}
	
	
}
