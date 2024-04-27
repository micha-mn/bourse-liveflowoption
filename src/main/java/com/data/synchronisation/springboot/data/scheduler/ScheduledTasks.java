package com.data.synchronisation.springboot.data.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseService;

@Component
public class ScheduledTasks {
    private RestTemplate restTemplate;
    private String serviceName;
    private CryptoAnalyseService cryptoAnalyseService;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public ScheduledTasks(RestTemplate restTemplate,CryptoAnalyseService cryptoAnalyseService) {
        this.serviceName = this.getClass().getName();
        this.restTemplate = restTemplate;
        this.cryptoAnalyseService = cryptoAnalyseService;
    }
	
	@Scheduled(fixedRate = 180000)
	public void reportCurrentTime() {
		log.info("The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
	    	String url = "https://www.binance.com/api/v3/ticker/price?symbol=";
	    	String[] currencies = {"BTCUSDT", "DOGEUSDT", "ETHFIUSDT","ENAUSDT","WUSDT"};
	    	for (int i=0;i<currencies.length;i++) {
	    		url = "https://www.binance.com/api/v3/ticker/price?symbol=";
	    		url = url +currencies[i];
	    		System.out.println("url: "+url);
	    		/*
	    		ResponseEntity<PriceCryptoRespDTO[]> response =
	  		          restTemplate.exchange(
	  		        		url,
	  		        		  HttpMethod.GET,
	  		        		  entity,
	  		        		  PriceCryptoRespDTO[].class);
	    		PriceCryptoRespDTO[] priceCryptoRespLst = response.getBody();
	    		*/
	    		ResponseEntity<PriceCryptoRespDTO> response =
		  		          restTemplate.exchange(
		  		        		url,
		  		        		  HttpMethod.GET,
		  		        		  entity,
		  		        		  PriceCryptoRespDTO.class);
	    		
		    	PriceCryptoRespDTO priceCryptoResp = response.getBody();
	    		System.out.println(priceCryptoResp);
	    		cryptoAnalyseService.scheduledServiceDataSynchronization(priceCryptoResp);
	    	}
	   
	    	log.info("The time is now {} ended {}", dateFormat.format(new Date()), new Date());	
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}