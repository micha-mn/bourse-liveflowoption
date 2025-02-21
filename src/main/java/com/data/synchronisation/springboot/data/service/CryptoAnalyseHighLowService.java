package com.data.synchronisation.springboot.data.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.synchronisation.springboot.data.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.domain.entity.CrBTCHighLow;
import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;
import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;
import com.data.synchronisation.springboot.enums.CryptoSymbol;
import com.data.synchronisation.springboot.repositories.CrBTCHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrBinanceHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrEthereumHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrShibaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrSolanaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrXrpHighLowRepository;


@Service
public class CryptoAnalyseHighLowService {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final Logger log = LoggerFactory.getLogger(CryptoAnalyseHighLowService.class);

	private final RestTemplate restTemplate = new RestTemplate();
	private static final String BINANCE_KLINE_URL = "https://api.binance.com/api/v3/klines";
	private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets";

	@Autowired
	CrBTCHighLowRepository crBTCHighLowRepository;
	@Autowired
	CrBinanceHighLowRepository crBinanceHighLowRepository;
	@Autowired
	CrXrpHighLowRepository crXrpHighLowRepository;
	@Autowired
	CrShibaHighLowRepository crShibaHighLowRepository;
	@Autowired
	CrSolanaHighLowRepository crSolanaHighLowRepository;
	@Autowired
	CrEthereumHighLowRepository crEthereumHighLowRepository;

	@Autowired
	private EntityManager entityManager;

	// Cryptocurrencies to fetch data for
	private static final String[] BINANCE_SYMBOLS = { "BTCUSDT", "ETHUSDT", "SOLUSDT", "SHIBUSDT", "BNBUSDT",
			"XRPUSDT" };
	
	private static final String[] COINGECKO_SYMBOLS = { "bitcoin", "ethereum", "solana", "shiba-inu", "binancecoin",
			"ripple" };
	
	public void fetchCryptoData(LocalDateTime startTime, LocalDateTime endTime) {
		System.out.println("Fetching data at: " + LocalDateTime.now());

		Map<String, Double> MarketCapData = fetchMarketCapData();

		for (int i = 0; i < BINANCE_SYMBOLS.length; i++) {
			String binanceSymbol = BINANCE_SYMBOLS[i]; // Binance symbol
			String coingeckoSymbol = COINGECKO_SYMBOLS[i]; // CoinGecko ID

			System.out.println("Fetching 3 minutes cacndle data for symbol: " + binanceSymbol);

			// Fetch OHLC data from Binance
			Map<String, Object> intlData = fetchKlineData(binanceSymbol, "1m", startTime, endTime);

			Double marketCap = MarketCapData.getOrDefault(coingeckoSymbol, 0.0);
			BigDecimal marketCapValue = BigDecimal.valueOf(marketCap);

			if (binanceSymbol.equalsIgnoreCase("BTCUSDT") && !intlData.isEmpty()) {

				CrBTCHighLow entity = CrBTCHighLow.builder()
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString()))
						.startTime(startTime)
						.endTime(endTime)
						.startTimeStamp(Long.valueOf(intlData.getOrDefault("startTime", "0").toString()))
						.endTimeStamp(Long.valueOf(intlData.getOrDefault("endTime", "0").toString()))
						.referDate(LocalDateTime.now())
						.build();

				// Save entity to the database
				crBTCHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("BNBUSDT") && !intlData.isEmpty()) {

				CrBinanceHighLow entity = CrBinanceHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime)
						.referDate(LocalDateTime.now())
						.build();
				
				// Save entity to the database
				crBinanceHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("ETHUSDT") && !intlData.isEmpty()) {

				CrEthereumHighLow entity = CrEthereumHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime)
						.referDate(LocalDateTime.now())
						.build();

				// Save entity to the database
				crEthereumHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("SOLUSDT") && !intlData.isEmpty()) {

				CrSolanaHighLow entity = CrSolanaHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime)
						.referDate(LocalDateTime.now())
						.build();

				// Save entity to the database
				crSolanaHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("SHIBUSDT") && !intlData.isEmpty()) {

				CrShibaHighLow entity = CrShibaHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime)
						.referDate(LocalDateTime.now())
						.build();

				// Save entity to the database
				crShibaHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("XRPUSDT") && !intlData.isEmpty()) {

				CrXrpHighLow entity = CrXrpHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.open(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.close(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime)
						.referDate(LocalDateTime.now())
						.build();

				// Save entity to the database
				crXrpHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			}

			else {
				System.out.println("No data available for: " + binanceSymbol);
			}
		}
	}

	/**
	 * Fetch OHLC (Open, High, Low, Close, Volume) from Binance Kline API.
	 */
	private Map<String, Object> fetchKlineData(String symbol, String interval, LocalDateTime startTime,
			LocalDateTime endTime) {
		System.out.println("fetchKlineData before epocmili: startTime "+startTime);
		System.out.println("fetchKlineData:before epocmili endTime "+endTime);
		long startTimestamp = startTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		long endTimestamp = endTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		System.out.println("fetchKlineData epocmili: startTimestamp "+startTimestamp);
		System.out.println("fetchKlineData:epocmili endTimestamp "+endTimestamp);
		String url = UriComponentsBuilder.fromHttpUrl(BINANCE_KLINE_URL).queryParam("symbol", symbol)
				.queryParam("interval", interval).queryParam("startTime", startTimestamp)
				.queryParam("endTime", endTimestamp)
				.queryParam("limit", 1).toUriString();

		
		try {
			List<List<Object>> response = restTemplate.getForObject(url, List.class);
			Map<String, Object> data = new HashMap<>();

			if (response != null && !response.isEmpty()) {
				List<Object> firstCandle = response.get(0); // First candle
				List<Object> lastCandle = response.get(response.size() - 1); // Last candle
				
				data.put("startTime", firstCandle.get(0));
				data.put("open", firstCandle.get(1));
				data.put("close", lastCandle.get(4));
				data.put("high", lastCandle.get(2));
				data.put("low", lastCandle.get(3));
				data.put("volume", lastCandle.get(5));
				data.put("endTime", firstCandle.get(6));
			}
			return data;
		} catch (Exception e) {
			System.out.println("Error fetching OHLC data for " + symbol + ": " + e.getMessage());
			return Collections.emptyMap();
		}
	}
	
		/**
		 * Fetch market cap data from CoinGecko for multiple cryptocurrencies.
		 */
		private Map<String, Double> fetchMarketCapData() {
			String symbols = String.join(",", COINGECKO_SYMBOLS);
			String url = UriComponentsBuilder.fromHttpUrl(COINGECKO_API_URL).queryParam("vs_currency", "usd")
					.queryParam("ids", symbols).toUriString();
	
			try {
				String response = restTemplate.getForObject(url, String.class);
				JSONArray jsonArray = new JSONArray(response);
				Map<String, Double> marketCapData = new HashMap<>();
	
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id = jsonObject.getString("id");
					double marketCap = jsonObject.getDouble("market_cap");
					marketCapData.put(id, marketCap);
				}
				System.out.println("marketCapData -- " + marketCapData);
				return marketCapData;
			} catch (Exception e) {
				log.error("Error fetching market cap data from CoinGecko: " + e.getMessage());
				return Collections.emptyMap();
			}
		}

		public void runDailyCryptoTask(String fromDate, String toDate) {
			
			for (int i = 0; i < BINANCE_SYMBOLS.length; i++) {
			
		        CryptoSymbol symbol = CryptoSymbol.fromString(BINANCE_SYMBOLS[i]);
		    	String tableName = symbol.getTableName(); 
		    	String groupId = symbol.getGroupId(); 
		    	
		   		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_dynamic_calculation_daily_data");
		   		query.registerStoredProcedureParameter("fromDate", String.class, ParameterMode.IN);
				query.setParameter("fromDate", fromDate);
				query.registerStoredProcedureParameter("toDateDate", String.class, ParameterMode.IN);
				query.setParameter("toDateDate", toDate);
				query.registerStoredProcedureParameter("tableName", String.class, ParameterMode.IN);
				query.setParameter("tableName", tableName);
				query.registerStoredProcedureParameter("groupId", String.class, ParameterMode.IN);
				query.setParameter("groupId", groupId);
				query.execute();
				
				 entityManager.clear();  // Keep clear() to free resources, but remove close()
			}
		}

	
}
