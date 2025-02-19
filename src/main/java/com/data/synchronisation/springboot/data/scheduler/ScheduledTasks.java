package com.data.synchronisation.springboot.data.scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONObject;
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
import com.data.synchronisation.springboot.data.dto.TradeInfoDTO;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseHighLowService;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseService;
import com.data.synchronisation.springboot.domain.entity.EnaTrackingTable;
import com.data.synchronisation.springboot.domain.entity.EthFITrackingTable;
import com.data.synchronisation.springboot.repositories.EnaTrackingRepository;
import com.data.synchronisation.springboot.repositories.EthFITrackingRepository;
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
	private EthFITrackingRepository ethFITrackingRepository;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private CryptoAnalyseHighLowService cryptoAnalyseHighLowService;
	public ScheduledTasks(RestTemplate restTemplate,
			CryptoAnalyseService cryptoAnalyseService,
			EnaTrackingRepository enaTrackingRepository,
			EthFITrackingRepository ethFITrackingRepository,
			CryptoAnalyseHighLowService cryptoAnalyseHighLowService) {
		
        this.serviceName = this.getClass().getName();
        this.restTemplate = restTemplate;
        this.cryptoAnalyseService = cryptoAnalyseService;
        this.enaTrackingRepository = enaTrackingRepository;
        this.ethFITrackingRepository = ethFITrackingRepository;
        this.cryptoAnalyseHighLowService = cryptoAnalyseHighLowService ;
    }
	
	
	
	@Scheduled(fixedRate = 20000 ) // 20000   300000
	public void syncLiveCurrencyPriceANdCalculateMinMax() {
		log.info("The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    String[] coinGeckoIds = {"bitcoin", "ethereum", "dogecoin", "binancecoin"};
	    try {
	    	
	    	String coinGeckoId = coinGeckoIds[0];
	    	String url = "https://www.binance.com/api/v3/ticker/price?symbols=";
	    	List<String> listOfCurrencies=new ArrayList<String>();
	    	listOfCurrencies.add("BTCUSDT");
	    	//listOfCurrencies.add("DOGEUSDT");
	    	//listOfCurrencies.add("ETHFIUSDT");
	    	//listOfCurrencies.add("ENAUSDT");
	    	//listOfCurrencies.add("WUSDT");
	    	//listOfCurrencies.add("SAGAUSDT");
	    	//listOfCurrencies.add("BNBUSDT");
	    	//listOfCurrencies.add("ETHUSDT");
	    	//listOfCurrencies.add("PEPEUSDT");
	    	//listOfCurrencies.add("FLOKIUSDT");
	    	
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
	    	
	    	// save live prices and caclulate min max
	    	cryptoAnalyseService.saveLivePrices(response.getBody());
	    	
	    	
	   
	    	log.info("The time is now {} ended {}", dateFormat.format(new Date()), new Date());	
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}

    
	// @Scheduled(fixedRate = 200000)  // 20000   300000
	public void syncHistoricalTradeEnaInfo() {
		log.info("syncTradeInfo The time is now {} started {}", dateFormat.format(new Date()), new Date());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    
	    HttpEntity entity = new HttpEntity<>(headers);
	    try {
	    	List<String> currList = new ArrayList<String>();
	    	currList.add(0, "ENNA");
	    	currList.add(0, "ETHFI");
	    	
	    	currList.forEach(crypto -> {
	    		    if(crypto.equals("ENNA")) {
				    		Optional<EnaTrackingTable> enaTrackingOpt ;
					    	EnaTrackingTable enaTracking = EnaTrackingTable.builder().build();
					    	enaTrackingOpt = enaTrackingRepository.findById(Long.valueOf("1"));
					    	String historyTradeUrl = "https://api.binance.com/api/v3/historicalTrades?symbol=ENAUSDT&limit=5000";
							String lastHistoricalDataId = null;
							LocalDateTime lastHistoricalDataDateExecuted = null;
							Long diffInMinutes = 0L;
							if(enaTrackingOpt.isPresent())
							{
								enaTracking = enaTrackingOpt.get();
								if(enaTracking.getLastHistoricalDataId() != null && enaTracking.getLastHistoricalDataDateExecuted() != null) {
									diffInMinutes = enaTracking.getLastHistoricalDataDateExecuted().until(LocalDateTime.now(), ChronoUnit.MINUTES);
									// ldt1.until(ldt2, ChronoUnit.HOURS));
									if(diffInMinutes<10) {
									lastHistoricalDataId = enaTracking.getLastHistoricalDataId();
									historyTradeUrl = historyTradeUrl +"&fromId="+lastHistoricalDataId;
									}
								}
							}	
					    	ResponseEntity<TradeInfoDTO[]> response =
					  		          restTemplate.exchange(
					  		        		historyTradeUrl,
					  		        		  HttpMethod.GET,
					  		        		  entity,
					  		        		TradeInfoDTO[].class);
					    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
					    	TradeInfoDTO[] responselst = response.getBody();
					    	cryptoAnalyseService.saveHistoryTradeInfo(responselst,"ENA");
					    	
					    	
					    	StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor_history");
					   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
					   		query.setParameter("currencyCode","ENNA" );
					   		query.execute();
				    		}
	    		    
	    		    if(crypto.equals("ETHFI")) {
			    		Optional<EthFITrackingTable> ethFITrackingOpt ;
				    	EthFITrackingTable ethTracking = EthFITrackingTable.builder().build();
				    	ethFITrackingOpt = ethFITrackingRepository.findById(Long.valueOf("1"));
				    	String historyTradeUrl = "https://api.binance.com/api/v3/historicalTrades?symbol=ETHFIUSDT&limit=5000";
						String lastHistoricalDataId = null;
						LocalDateTime lastHistoricalDataDateExecuted = null;
						Long diffInMinutes = 0L;
						if(ethFITrackingOpt.isPresent())
						{
							ethTracking = ethFITrackingOpt.get();
							if(ethTracking.getLastHistoricalDataId() != null && ethTracking.getLastHistoricalDataDateExecuted()!= null) {
								diffInMinutes = ethTracking.getLastHistoricalDataDateExecuted().until(LocalDateTime.now(), ChronoUnit.MINUTES);
								// ldt1.until(ldt2, ChronoUnit.HOURS));
								if(diffInMinutes<10) {
								lastHistoricalDataId = ethTracking.getLastHistoricalDataId();
								historyTradeUrl = historyTradeUrl +"&fromId="+lastHistoricalDataId;
								}
							}
						}	
				    	ResponseEntity<TradeInfoDTO[]> response =
				  		          restTemplate.exchange(
				  		        		historyTradeUrl,
				  		        		  HttpMethod.GET,
				  		        		  entity,
				  		        		TradeInfoDTO[].class);
				    	//cryptoAnalyseService.scheduledServiceDataSynchronization(response.getBody());
				    	TradeInfoDTO[] responselst = response.getBody();
				    	cryptoAnalyseService.saveHistoryTradeInfo(responselst,"ETHFI");
				    	
				    	
				    	StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor_history");
				   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
				   		query.setParameter("currencyCode","ETHFI" );
				   		query.execute();
			    		}
    		
	    		
	    	});
	    	
		    	
		   		
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
	
	
	
	/*	 1625893200000,   // Open time
        "34479.90",       // Open price
        "34480.00",       // High price
        "34479.80",       // Low price
        "34480.00",       // Close price
        "0.003",          // Volume
        1625893259999,    // Close time
        "103.44",         // Quote asset volume
        1,                // Number of trades
        "0",              // Taker buy base asset volume
        "0",              // Taker buy quote asset volume
        "0"               // Ignore
        
        >>  Market Capitalization (market_cap):
		Definition: The market cap is the total value of all coins currently in circulation.
		
		Formula:Market Cap = Current Price × Circulating Supply
		Market Cap=Current Price×Circulating Supply
		The market cap only includes coins or tokens that are currently available in the market.
        
        
        
         >> Fully Diluted Valuation (fully_diluted_valuation):
		Definition: The fully diluted valuation assumes that all possible coins or tokens (whether currently in 
		        circulation or not) have been issued and are factored into the valuation.
		
		Formula: Fully Diluted Valuation = Current Price × Max Supply
		Fully Diluted Valuation=Current Price×Max Supply
		Key Point:
		The fully diluted valuation represents the potential future value if all coins were issued, which is typically higher than the market cap.
        -- --------------------------------
        -- --------------------------------
        accumulation distribution model
		manipulation area
		declining volume
		price action
		trend line
		
        */
	    @Scheduled(cron = "0 0/1 * * * ?")
		//@Scheduled(cron = "*/1 * * * * ?")
		public void schedule1HourIntervals() {

			// Calculate the current interval's start and end times
	    	
			LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
			
			// End time: rounded down to the nearest 5-minute interval
	       LocalDateTime endTime = now.withMinute((now.getMinute() / 1) * 1).withSecond(0).withNano(0);
	        LocalDateTime startTime = endTime.minusMinutes(1);
	        
	      //  LocalDateTime endTime = now.withNano(0);
	      //  LocalDateTime startTime = endTime.minusSeconds(1);
	        
			System.out.println("now --" + now);
			System.out.println("startTime --" + startTime);
			System.out.println("endTime --" + endTime);

			cryptoAnalyseHighLowService.fetchCryptoData(startTime, endTime);

			System.out.println("schedule1HourIntervals Task Executed: " + startTime + " to " + endTime);
		}
	    
	    @Scheduled(cron = "0 5 0 * * ?")// Runs at 00:05 AM every day
	   public void runDailyCryptoTask() {
	        System.out.println("Scheduled task running at 00:05 AM...");

	        // Get yesterday's date
	        LocalDate yesterday = LocalDate.now().minusDays(1);
	        
	        // Format the date (YYYY-MM-DD format)
	        String fromDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
	        String toDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:00";
	        
	        cryptoAnalyseHighLowService.runDailyCryptoTask(fromDate, toDate);
	       
			System.out.println("runDailyCryptoTask Task Executed: " + fromDate + " to " + toDate);
		}
	    
	    /**
	     *   
	        working on 3 models out of 7 - calculated function
	        remove market noise
	     	accumilation distribution model
			manipulation area
			declining volume
			price action
			trend line
			volatility analysis (moving in strong way)
			liquidity
			movement
			moving average
			trendline
			retracement
			resistant point
			support point
			first resistent level
			second resistent point
			bollinger
	     * */
	    
	    /*
	     * bollinger
	     * 
	     * 
	     * 
	     * three lines plotted on a price chart:

			Middle Band: A simple moving average (SMA), typically set to 20 periods.
			
			Upper Band: Calculated by adding a multiple (usually 2) of the standard deviation to the middle band.
			
			Lower Band: Calculated by subtracting the same multiple of the standard deviation from the middle band.
	     */
	    
	    /*
	     * 
	     * Combine Bollinger Bands with other candlestick signals like engulfing patterns, doji, or hammer candlesticks to strengthen trade accuracy.
	     * 
	     * 
	     * 
	     * 
	     * 1. Pivot Point Formula
				Support Levels
	     *             Support 1 (S1):
	     *             Support 2 (S2):
	     * 
	     * 
	     * */
	
	
}