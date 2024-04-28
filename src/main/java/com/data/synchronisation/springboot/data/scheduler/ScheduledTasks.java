package com.data.synchronisation.springboot.data.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseService;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.Gson;

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
	
	@Scheduled(fixedRate = 10000)
	public void reportCurrentTime() {
		log.info("The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
	    	String url = "https://www.binance.com/api/v3/ticker/price?symbols=";
	    	List<String> listOfCurrencies=new ArrayList<String>();
	    	listOfCurrencies.add("BTCUSDT");
	    	listOfCurrencies.add("DOGEUSDT");
	    	listOfCurrencies.add("ETHFIUSDT");
	    	listOfCurrencies.add("ENAUSDT");
	    	listOfCurrencies.add("WUSDT");
	    	
			String levelPattern = new Gson().toJson(listOfCurrencies, ArrayList.class);
			url = url +levelPattern;
			
			System.out.println(levelPattern);
			List<String> values = new ArrayList<>();
			
			
	    	ResponseEntity<PriceCryptoRespDTO[]> response =
	  		          restTemplate.exchange(
	  		        		url,
	  		        		  HttpMethod.GET,
	  		        		  entity,
	  		        		  PriceCryptoRespDTO[].class);
	    	
	    	cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
	    	
	    	
	   
	    	log.info("The time is now {} ended {}", dateFormat.format(new Date()), new Date());	
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}