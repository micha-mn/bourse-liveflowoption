package com.data.synchronisation.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBook;
import com.data.synchronisation.springboot.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.dto.OrderBookByActionObjectProjection;
import com.data.synchronisation.springboot.dto.OrderBookResponseDTO;
import com.data.synchronisation.springboot.dto.orderBook.OrderBookBidAskListDTO;
import com.data.synchronisation.springboot.dto.orderBook.OrderBookDataDTO;
import com.data.synchronisation.springboot.dto.orderBook.OrderBookPercentageResponseDTO;
import com.data.synchronisation.springboot.dto.prices.BtcDTO;
import com.data.synchronisation.springboot.dto.prices.CurrencyPreviousPriceDTO;
import com.data.synchronisation.springboot.repositories.BtcRepository;
import com.data.synchronisation.springboot.repositories.CRBTCOrderBookConsolidatedRepository;
import com.data.synchronisation.springboot.repositories.CRBTCOrderBookRepository;

@Service
public class OrderBookService {

	 @Autowired
		CRBTCOrderBookRepository btcOrderBookRepository;
	 @Autowired
	 CRBTCOrderBookConsolidatedRepository  crBTCOrderBookConsolidatedRepository;

	 
	 @Autowired
	 PriceService  priceService;
	
	 
	 public void saveOrderBookLst(List<CRBTCOrderBook> binanceOrderBookLst) {
		 System.out.println("saving Orders");
		 btcOrderBookRepository.saveAll(binanceOrderBookLst);
	     
	 }
	 
     public List<GraphResponseProjection> getOrderBookPercentage(GraphDataReqDTO req) {
    	
    	if(req.getHmd().equalsIgnoreCase("HOUR"))
    		return crBTCOrderBookConsolidatedRepository.getOrderBookConsolidatedHourPeriod(req.getPeriod());
    	if(req.getHmd().equalsIgnoreCase("MINUTE"))
    		return crBTCOrderBookConsolidatedRepository.getOrderBookConsolidatedMinutePeriod(req.getPeriod());
    	
    	return new ArrayList<>();
    		
	 }
     
     public OrderBookResponseDTO getOrderBookByBidAsk(GraphDataReqDTO req) {
     	
    	 List<OrderBookByActionObjectProjection>  orderBookLstBuy = crBTCOrderBookConsolidatedRepository.getOrderBookByActionDesc(req.getLimit(), "buy");
    	 List<OrderBookByActionObjectProjection>  orderBookLstSell = crBTCOrderBookConsolidatedRepository.getOrderBookByActionAsc(req.getLimit(), "sell");
    	 
    	 OrderBookResponseDTO resp = OrderBookResponseDTO.builder()
    			 .ask(orderBookLstBuy)
    			 .bid(orderBookLstSell)
    			 .build();
    	 return resp;
     		
 	 }
     
     public OrderBookDataDTO getOrderBookData(GraphDataReqDTO req) {
      	
    	 
    	 
    	 if(req.getCryptoCurrencyCode().equalsIgnoreCase("btc"))
    	 {
	    	 List<OrderBookByActionObjectProjection>  orderBookLstBuy = crBTCOrderBookConsolidatedRepository.getOrderBookByActionDesc(req.getLimit(), "buy");
	    	 List<OrderBookByActionObjectProjection>  orderBookLstSell = crBTCOrderBookConsolidatedRepository.getOrderBookByActionAsc(req.getLimit(), "sell");
	    	
	    	 List<GraphResponseProjection> orderBookPercentage = getOrderBookPercentage(req) ;
	    	 
	    	 OrderBookPercentageResponseDTO orderBookPercent = OrderBookPercentageResponseDTO.builder()
	    			 .orderBookPercentage(orderBookPercentage)
	    			 .build();
	    	 
	    	 OrderBookBidAskListDTO orderBookData = OrderBookBidAskListDTO.builder().ask(orderBookLstBuy).bid(orderBookLstSell).build();
	    	 
	    	 List<BtcDTO> btcDTOLst = priceService.getCurrentPreviousPrice();
	    	 
	    	 CurrencyPreviousPriceDTO currencyPreviousPriceDTO = CurrencyPreviousPriceDTO.builder()
	    			 .currentPrice(btcDTOLst.get(0).getValue())
	    			 .previousPrice(btcDTOLst.get(1).getValue())
	    			 .build();
	    	 
	    	 OrderBookDataDTO orderBookDataDTO = OrderBookDataDTO.builder()
	    			 .currencyPreviousPriceDTO(currencyPreviousPriceDTO)
	    			 .orderBookDataBidAsk(orderBookData)
	    			 .orderBookPercentage(orderBookPercent)
	    			 .build();
	    	 
	    	 return orderBookDataDTO;
    	 }
    	 
    	 return null;
    	 
    	 
     		
 	 }
     
	 
	 
	 
	    
}
