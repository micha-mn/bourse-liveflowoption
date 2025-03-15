package com.data.synchronisation.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBook;
import com.data.synchronisation.springboot.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.repositories.CRBTCOrderBookConsolidatedRepository;
import com.data.synchronisation.springboot.repositories.CRBTCOrderBookRepository;

@Service
public class OrderBookService {

	 @Autowired
		CRBTCOrderBookRepository btcOrderBookRepository;
	 @Autowired
	 CRBTCOrderBookConsolidatedRepository  crBTCOrderBookConsolidatedRepository;
	 
	 public OrderBookService(CRBTCOrderBookRepository btcOrderBookRepository) {
	        // Enable Spring to inject dependencies into this endpoint
	       // SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	    	this.btcOrderBookRepository = btcOrderBookRepository;
	    }
	 
	 
	 public void saveOrderBookLst(List<CRBTCOrderBook> binanceOrderBookLst) {
		 System.out.println("saving Orders");
		 btcOrderBookRepository.saveAll(binanceOrderBookLst);
	     
	 }
	 
     public List<GraphResponseProjection> getOrderBookData(GraphDataReqDTO req) {
    	return crBTCOrderBookConsolidatedRepository.getOrderBookConsolidated(req.getMinutes());
	 }
	 
	 
	 
	    
}
