package com.data.synchronisation.springboot.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.data.synchronisation.springboot.domain.entity.CrBTCHighLow;
import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;
import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;
import com.data.synchronisation.springboot.dto.CurrencyDTO;
import com.data.synchronisation.springboot.dto.DataDTO;
import com.data.synchronisation.springboot.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.dto.GraphFulllResponseDTO;
import com.data.synchronisation.springboot.dto.GraphGeneralResponseDTO;
import com.data.synchronisation.springboot.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.dto.OrderBookResponseDTO;
import com.data.synchronisation.springboot.dto.SupportResistantPointsDTO;
import com.data.synchronisation.springboot.dto.TradeHistoryResDTO;
import com.data.synchronisation.springboot.dto.TradeReqDTO;
import com.data.synchronisation.springboot.dto.TradeResponseDTO;
import com.data.synchronisation.springboot.dto.orderBook.OrderBookDataDTO;
import com.data.synchronisation.springboot.service.CryptoAnalyseHighLowService;
import com.data.synchronisation.springboot.service.CryptoAnalyseService;
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
	
	@GetMapping(value = "/get-order-book-data")
	public OrderBookDataDTO getOrderBookData(@RequestBody GraphDataReqDTO req) {
		// return null;
		 return orderBookService.getOrderBookData(req);
	}
	
	
}
