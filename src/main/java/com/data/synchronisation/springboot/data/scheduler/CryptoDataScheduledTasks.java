package com.data.synchronisation.springboot.data.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrBitcoinHighLow;
import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;
import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;
import com.data.synchronisation.springboot.repositories.CrBinanceHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrBitcoinHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrEthereumHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrShibaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrSolanaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrXrpHighLowRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Service
public class CryptoDataScheduledTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final Logger log = LoggerFactory.getLogger(CryptoDataScheduledTasks.class);

	private final RestTemplate restTemplate = new RestTemplate();
	private static final String BINANCE_KLINE_URL = "https://api.binance.com/api/v3/klines";
	private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets";

	@Autowired
	CrBitcoinHighLowRepository crBitcoinHighLowRepository;
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

			System.out.println("Fetching data for symbol: " + binanceSymbol);
			// ✅ Get current UTC time
			LocalDateTime nowUtc = LocalDateTime.now(ZoneId.of("UTC"));

			// Calculate Euro-time range
			LocalDateTime euroEnd = nowUtc.atZone(ZoneId.of("CET")).toLocalDateTime();
			LocalDateTime euroStart = euroEnd.minusMinutes(1);

			// Calculate International-time range
			LocalDateTime intlEnd = nowUtc;
			LocalDateTime intlStart = intlEnd.minusMinutes(1);

			// Fetch OHLC data from Binance
			Map<String, Object> intlData = fetchKlineData(binanceSymbol, "1h", startTime, endTime);

			Double marketCap = MarketCapData.getOrDefault(coingeckoSymbol, 0.0);
			BigDecimal marketCapValue = BigDecimal.valueOf(marketCap);

			if (binanceSymbol.equalsIgnoreCase("BTCUSDT") && !intlData.isEmpty()) {

				CrBitcoinHighLow entity = CrBitcoinHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();

				// Save entity to the database
				crBitcoinHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("BNBUSDT") && !intlData.isEmpty()) {

				CrBinanceHighLow entity = CrBinanceHighLow.builder()
						// Save Euro-time OHLC data
						.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();
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
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();

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
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();

				// Save entity to the database
				crSolanaHighLowRepository.save(entity);
				System.out.println("Saved combined data for: " + binanceSymbol);
			} else if (binanceSymbol.equalsIgnoreCase("SHIBUSDT") && !intlData.isEmpty()) {

			   // BigDecimal scaleFactor = new BigDecimal("1000");

			    CrShibaHighLow entity = CrShibaHighLow.builder()
			            // Save Euro-time OHLC data, scaling each value appropriately
			          /*  .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()).multiply(scaleFactor))
			            .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()).multiply(scaleFactor))
			            .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString())) // Volume stays as is
			            .marketcap(marketCapValue) // Assuming marketCapValue is already properly formatted
			            .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()).multiply(scaleFactor))
			            .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()).multiply(scaleFactor))
			            .startTime(startTime)
			            .endTime(endTime)
			          */
			    		.high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
						.low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
						.volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
						.marketcap(marketCapValue) // Remove commas and convert
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();

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
						.openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
						.closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString())).startTime(startTime)
						.endTime(endTime).build();

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
		long startTimestamp = startTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		long endTimestamp = endTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

		String url = UriComponentsBuilder.fromHttpUrl(BINANCE_KLINE_URL).queryParam("symbol", symbol)
				.queryParam("interval", interval).queryParam("startTime", startTimestamp)
				.queryParam("endTime", endTimestamp).toUriString();

		try {
			List<List<Object>> response = restTemplate.getForObject(url, List.class);
			Map<String, Object> data = new HashMap<>();

			if (response != null && !response.isEmpty()) {
				List<Object> firstCandle = response.get(0); // First candle
				List<Object> lastCandle = response.get(response.size() - 1); // Last candle

				data.put("open", firstCandle.get(1));
				data.put("close", lastCandle.get(4));
				data.put("high", lastCandle.get(2));
				data.put("low", lastCandle.get(3));
				data.put("volume", lastCandle.get(5));
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

	public void callInsertCryptosProcedure(String startTime, String endTime, String startTimeCet, String endTimeCet) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_cryptos_4_hour_data");

		// Register input parameters
		query.registerStoredProcedureParameter("utc_start_time", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("utc_end_time", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("cet_start_time", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("cet_end_time", String.class, ParameterMode.IN);

		// Set input parameter values
		query.setParameter("utc_start_time", startTime);
		query.setParameter("utc_end_time", endTime);
		query.setParameter("cet_start_time", startTimeCet);
		query.setParameter("cet_end_time", endTimeCet);

		// Execute the procedure
		query.execute();
	}

	// @Scheduled(cron = "0 0 */1 * * *")
	public void schedule1HourIntervals() {

		// Calculate the current interval's start and end times
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime startTime = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime endTime = now.withMinute(0).withSecond(0).withNano(0).minusSeconds(1);

		System.out.println("now --" + now);
		System.out.println("startTime --" + startTime);
		System.out.println("endTime --" + endTime);

		fetchCryptoData(startTime, endTime);

		System.out.println("schedule1HourIntervals Task Executed: " + startTime + " to " + endTime);
	}

	//@Scheduled(cron = "0 0 */4 * * *") // Runs at 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
	public void schedule4HourIntervals() {

		// Calculate the current interval's start and end times
		LocalDateTime nowUTC = LocalDateTime.now(ZoneOffset.UTC);
		// Calculate the original UTC start and end times
		LocalDateTime startTimeUTC = nowUTC.minusHours(4).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime endTimeUTC = nowUTC.withMinute(0).withSecond(0).withNano(0).minusSeconds(1);

		// Convert the start and end times to CET, letting Java handle daylight savings
		ZonedDateTime startTimeCET = startTimeUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("CET"));
		ZonedDateTime endTimeCET = endTimeUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("CET"));

		// Calculate the dynamic time difference between UTC and CET
		int utcOffsetHoursStart = startTimeCET.getOffset().getTotalSeconds() / 3600; // Get offset in hours
		int utcOffsetHoursEnd = endTimeCET.getOffset().getTotalSeconds() / 3600;

		// Adjust UTC times by the offset
		LocalDateTime adjustedStartTimeUTC = startTimeUTC.minusHours(utcOffsetHoursStart);
		LocalDateTime adjustedEndTimeUTC = endTimeUTC.minusHours(utcOffsetHoursEnd);

		String start = startTimeUTC.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String end = endTimeUTC.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		String startCet = adjustedStartTimeUTC.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String endCet = adjustedEndTimeUTC.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// Call the stored procedure for this interval
		System.out.println("START TIME= " + start);
		System.out.println("END TIME= " + end);

		System.out.println("START TIME CET = " + startCet);
		System.out.println("END TIME CET= " + endCet);
		callInsertCryptosProcedure(start, end, startCet, endCet);
		System.out.println("-- time  " + start + " to " + end + " CET " + start + " to " + end);

		System.out.println("schedule4HourIntervals Task Executed: " + startTimeUTC + " to " + endTimeUTC);
	}
}
