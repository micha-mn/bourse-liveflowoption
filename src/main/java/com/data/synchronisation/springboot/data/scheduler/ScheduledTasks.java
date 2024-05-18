package com.data.synchronisation.springboot.data.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.data.synchronisation.springboot.data.dto.CurrencyInfoDTO;
import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.dto.TradeInfoDTO;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseService;
import com.data.synchronisation.springboot.domain.entity.EnaTrackingTable;
import com.data.synchronisation.springboot.repositories.EnaTrackingRepository;
import com.google.gson.Gson;

@Component
public class ScheduledTasks {
	@PersistenceContext
    private EntityManager entityManager;
	
    private RestTemplate restTemplate;
    private String serviceName;
    private CryptoAnalyseService cryptoAnalyseService;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private EnaTrackingRepository enaTrackingRepository;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public ScheduledTasks(RestTemplate restTemplate,CryptoAnalyseService cryptoAnalyseService,
			EnaTrackingRepository enaTrackingRepository) {
        this.serviceName = this.getClass().getName();
        this.restTemplate = restTemplate;
        this.cryptoAnalyseService = cryptoAnalyseService;
        this.enaTrackingRepository = enaTrackingRepository;
    }
	
	/*
	
	@Scheduled(fixedRate = 30000 ) // 20000   300000
	public void syncCurrencyPrice() {
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
	    	listOfCurrencies.add("SAGAUSDT");
	    	listOfCurrencies.add("BNBUSDT");
	    	listOfCurrencies.add("ETHUSDT");
	    	listOfCurrencies.add("PEPEUSDT");
	    	listOfCurrencies.add("FLOKIUSDT");
	    	
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
	    	
	    	cryptoAnalyseService.scheduledServiceCurrencyDataSynchronization(response.getBody());
	    	
	    	
	   
	    	log.info("The time is now {} ended {}", dateFormat.format(new Date()), new Date());	
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	*/
	
	
	@Scheduled(fixedRate = 20000)  // 20000   300000
	public void syncHistoricalTradeEnaInfo() {
		log.info("syncTradeInfo The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
	    	Optional<EnaTrackingTable> enaTrackingOpt ;
	    	EnaTrackingTable enaTracking = EnaTrackingTable.builder().build();
	    	enaTrackingOpt = enaTrackingRepository.findById(Long.valueOf("1"));
	    	String marketCapUrl = "https://api.binance.com/api/v3/historicalTrades?symbol=ENAUSDT&limit=5000";
			String lastHistoricalDataId = null;
			if(enaTrackingOpt.isPresent())
			{
				enaTracking = enaTrackingOpt.get();
				if(enaTracking.getLastHistoricalDataId() != null) {
					lastHistoricalDataId = enaTracking.getLastHistoricalDataId();
					marketCapUrl = marketCapUrl +"&fromId="+lastHistoricalDataId;
				}
				
			}
	    	// https://api.binance.com/api/v3/historicalTrades?symbol=ETHUSDT&fromId=1423720033&limit=5000
		    	
		    	
		    	ResponseEntity<TradeInfoDTO[]> response =
		  		          restTemplate.exchange(
		  		        		marketCapUrl,
		  		        		  HttpMethod.GET,
		  		        		  entity,
		  		        		TradeInfoDTO[].class);
		    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
		    	TradeInfoDTO[] responselst = response.getBody();
		    	cryptoAnalyseService.saveHistoryTradeInfo(responselst,"ENA");
		    	
		    	
		    	StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor_history");
		   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
		   		query.setParameter("currencyCode","ENA" );
		   		query.execute();
		    	
		   		
		   		/*
		    	marketCapUrl = "https://api.binance.com/api/v3/trades?symbol=WUSDT";
		    	response =
		  		          restTemplate.exchange(
		  		        		marketCapUrl,
		  		        		  HttpMethod.GET,
		  		        		  entity,
		  		        		TradeInfoDTO[].class);
		    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
		    	
		    	responselst = response.getBody();
		    	cryptoAnalyseService.saveTradeInfo(responselst,"W");
		    	
		    	TimeUnit.MILLISECONDS.sleep(10000);
		    	query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor");
		   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
		   		query.setParameter("currencyCode","W" );
		    	// query.registerStoredProcedureParameter("referDate", String.class, ParameterMode.IN);
		   		// query.setParameter("referDate",referDate );
		   		query.execute();
		   		*/
		   		
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/*

	@Scheduled(fixedRate = 3990005)
	public void syncMarketCapInfo() {
		log.info("syncCurrencyInfo The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
	    	
	    	
	    	String marketCapUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&ids=ethena,bitcoin,wormhole";
			
			
	    	ResponseEntity<CurrencyInfoDTO[]> response =
	  		          restTemplate.exchange(
	  		        		marketCapUrl,
	  		        		  HttpMethod.GET,
	  		        		  entity,
	  		        		  CurrencyInfoDTO[].class);
	    	
	    	
	    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
	    	CurrencyInfoDTO[] responselst = response.getBody();
	    	cryptoAnalyseService.saveCurrencyInfo(responselst);
	    	System.out.println(response);
	    	log.info("The time is now {} ended {}", dateFormat.format(new Date()), new Date());	
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(fixedRate = 20000)  // 20000   300000
	public void syncTradeInfo() {
		log.info("syncTradeInfo The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
		    	String marketCapUrl = "https://api.binance.com/api/v3/trades?symbol=ENAUSDT";
		    	ResponseEntity<TradeInfoDTO[]> response =
		  		          restTemplate.exchange(
		  		        		marketCapUrl,
		  		        		  HttpMethod.GET,
		  		        		  entity,
		  		        		TradeInfoDTO[].class);
		    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
		    	TradeInfoDTO[] responselst = response.getBody();
		    	cryptoAnalyseService.saveTradeInfo(responselst,"ENA");
		    	
		    	StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor");
		   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
		   		query.setParameter("currencyCode","ENA" );
		   		query.execute();
		    	
		    	marketCapUrl = "https://api.binance.com/api/v3/trades?symbol=WUSDT";
		    	response =
		  		          restTemplate.exchange(
		  		        		marketCapUrl,
		  		        		  HttpMethod.GET,
		  		        		  entity,
		  		        		TradeInfoDTO[].class);
		    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
		    	
		    	responselst = response.getBody();
		    	cryptoAnalyseService.saveTradeInfo(responselst,"W");
		    	
		    	TimeUnit.MILLISECONDS.sleep(10000);
		    	query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor");
		   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
		   		query.setParameter("currencyCode","W" );
		    	// query.registerStoredProcedureParameter("referDate", String.class, ParameterMode.IN);
		   		// query.setParameter("referDate",referDate );
		   		query.execute();
		   		
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	*/
	
	
}