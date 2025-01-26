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

import com.data.synchronisation.springboot.domain.entity.TmpCryBinanceSeconds;
import com.data.synchronisation.springboot.domain.entity.TmpCryBitcoinSeconds;
import com.data.synchronisation.springboot.domain.entity.TmpCryEthereumSeconds;
import com.data.synchronisation.springboot.domain.entity.TmpCryShibaSeconds;
import com.data.synchronisation.springboot.domain.entity.TmpCrySolanaSeconds;
import com.data.synchronisation.springboot.domain.entity.TmpCryXrpSeconds;
import com.data.synchronisation.springboot.repositories.TmpCryBinanceSecondsRepository;
import com.data.synchronisation.springboot.repositories.TmpCryBitcoinSecondsRepository;
import com.data.synchronisation.springboot.repositories.TmpCryEthereumSecondsRepository;
import com.data.synchronisation.springboot.repositories.TmpCryShibaSecondsRepository;
import com.data.synchronisation.springboot.repositories.TmpCrySolanaSecondsRepository;
import com.data.synchronisation.springboot.repositories.TmpCryXrpSecondsRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.*;

import javax.annotation.PostConstruct;
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
    TmpCryBitcoinSecondsRepository tmpCryBitcoinSecondsRepository;
    @Autowired
    TmpCryEthereumSecondsRepository tmpCryEthereumSecondsRepository;
    @Autowired
    TmpCrySolanaSecondsRepository tmpCrySolanaSecondsRepository;
    @Autowired
    TmpCryShibaSecondsRepository tmpCryShibaSecondsRepository;
    @Autowired
    TmpCryXrpSecondsRepository tmpCryXrpSecondsRepository;
    @Autowired
    TmpCryBinanceSecondsRepository tmpCryBinanceSecondsRepository;
    @Autowired
    private EntityManager entityManager;
    
    // Cryptocurrencies to fetch data for
    private static final String[] BINANCE_SYMBOLS = {"BTCUSDT", "ETHUSDT", "SOLUSDT", "SHIBUSDT", "BNBUSDT", "XRPUSDT"};
    private static final String[] COINGECKO_SYMBOLS = {"bitcoin", "ethereum", "solana", "shiba-inu", "binancecoin", "ripple"};

    private Map<String, Double> cachedMarketCapData = new HashMap<>();
    private LocalDateTime lastMarketCapFetchTime = LocalDateTime.now().minusMinutes(1);

    
    @Scheduled(fixedDelay = 5000) // Run every 5 seconds AFTER the last execution completes
    public void fetchCryptoData() {
        System.out.println("Fetching data at: " + LocalDateTime.now());


        // Check if 1 minute has passed since the last market cap fetch
        if (Duration.between(lastMarketCapFetchTime, LocalDateTime.now()).toSeconds() >= 60) {
            cachedMarketCapData = fetchMarketCapData();
            lastMarketCapFetchTime = LocalDateTime.now();
        }
        
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
            Map<String, Object> euroData = fetchKlineData(binanceSymbol, "1m", euroStart, euroEnd);
            Map<String, Object> intlData = fetchKlineData(binanceSymbol, "1m", intlStart, intlEnd);

            Double marketCap = cachedMarketCapData.getOrDefault(coingeckoSymbol, 0.0);
            BigDecimal marketCapValue = BigDecimal.valueOf(marketCap);
            
            if (binanceSymbol.equalsIgnoreCase("BTCUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            	
            	TmpCryBitcoinSeconds entity = TmpCryBitcoinSeconds.builder()
                        // Save Euro-time OHLC data
            			    .openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
            			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
            			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
            			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
            			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
            			    .marketcap(marketCapValue) // Remove commas and convert
            			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
            			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))
                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
                tmpCryBitcoinSecondsRepository.save(entity);
                System.out.println("Saved combined data for: " + binanceSymbol);
            } else if (binanceSymbol.equalsIgnoreCase("ETHUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            
            	TmpCryEthereumSeconds entity = TmpCryEthereumSeconds.builder()
                        // Save Euro-time OHLC data
            			.openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
          			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
          			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
          			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
          			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
          			    .marketcap(marketCapValue) // Remove commas and convert
          			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
          			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))

                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
            	tmpCryEthereumSecondsRepository.save(entity);
                System.out.println("Saved combined data for: " + binanceSymbol);
            }else if (binanceSymbol.equalsIgnoreCase("SOLUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            	
                
            	TmpCrySolanaSeconds entity = TmpCrySolanaSeconds.builder()
                        // Save Euro-time OHLC data
            			  .openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
          			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
          			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
          			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
          			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
          			    .marketcap(marketCapValue) // Remove commas and convert
          			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
          			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))

                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
            	tmpCrySolanaSecondsRepository.save(entity);
                System.out.println("Saved combined data for: " + binanceSymbol);
            }
            else if (binanceSymbol.equalsIgnoreCase("SHIBUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            	
            	TmpCryShibaSeconds entity = TmpCryShibaSeconds.builder()
                        // Save Euro-time OHLC data
            			  .openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
          			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
          			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
          			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
          			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
          			    .marketcap(marketCapValue) // Remove commas and convert
          			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
          			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))
                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
            	tmpCryShibaSecondsRepository.save(entity);
                System.out.println("Saved combined data for: " + binanceSymbol);
            }
            else if (binanceSymbol.equalsIgnoreCase("BNBUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            	
            	TmpCryBinanceSeconds entity = TmpCryBinanceSeconds.builder()
                        // Save Euro-time OHLC data
            			  .openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
          			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
          			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
          			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
          			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
          			    .marketcap(marketCapValue) // Remove commas and convert
          			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
          			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))

                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
            	tmpCryBinanceSecondsRepository.save(entity);
                System.out.println("Saved combined data for: " + binanceSymbol);
            } else if (binanceSymbol.equalsIgnoreCase("XRPUSDT") && !euroData.isEmpty() && !intlData.isEmpty()) {
            	
            	TmpCryXrpSeconds entity = TmpCryXrpSeconds.builder()
                        // Save Euro-time OHLC data
            			  .openeur(new BigDecimal(euroData.getOrDefault("open", "0").toString())) // Convert String to BigDecimal
          			    .closeeur(new BigDecimal(euroData.getOrDefault("close", "0").toString()))
          			    .high(new BigDecimal(intlData.getOrDefault("high", "0").toString()))
          			    .low(new BigDecimal(intlData.getOrDefault("low", "0").toString()))
          			    .volume(new BigDecimal(intlData.getOrDefault("volume", "0").toString()))
          			    .marketcap(marketCapValue) // Remove commas and convert
          			    .openint(new BigDecimal(intlData.getOrDefault("open", "0").toString()))
          			    .closeint(new BigDecimal(intlData.getOrDefault("close", "0").toString()))

                        .referDate(nowUtc)
                        .build();

                // Save entity to the database
            	tmpCryXrpSecondsRepository.save(entity);
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
    private Map<String, Object> fetchKlineData(String symbol, String interval, LocalDateTime startTime, LocalDateTime endTime) {
        long startTimestamp = startTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
        long endTimestamp = endTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

        String url = UriComponentsBuilder.fromHttpUrl(BINANCE_KLINE_URL)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("startTime", startTimestamp)
                .queryParam("endTime", endTimestamp)
                .toUriString();

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
        String url = UriComponentsBuilder.fromHttpUrl(COINGECKO_API_URL)
                .queryParam("vs_currency", "usd")
                .queryParam("ids", symbols)
                .toUriString();

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
System.out.println("marketCapData -- "+marketCapData);
            return marketCapData;
        } catch (Exception e) {
            log.error("Error fetching market cap data from CoinGecko: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
    public void callInsertCryptosProcedure(String startTime, String endTime) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_cryptos_4_hour_data");

        // Register input parameters
        query.registerStoredProcedureParameter("p_start_time", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_end_time", String.class, ParameterMode.IN);

        // Set input parameter values
        query.setParameter("p_start_time", startTime);
        query.setParameter("p_end_time", endTime);

        // Execute the procedure
        query.execute();
    }
    @PostConstruct
    public void runOnStartup() {
        System.out.println("Running the task immediately after startup.");
        ZoneId systemZone = ZoneId.systemDefault();

        // Print the system's timezone
        System.out.println("Current system timezone: " + systemZone);

        // Optionally, you can print the current time in that timezone
        ZonedDateTime currentTime = ZonedDateTime.now(systemZone);
        System.out.println("Current time in system timezone: " + currentTime);
        
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startTime = now.minusHours(4).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = now.withMinute(0).withSecond(0).withNano(0);
        String start = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        LocalDateTime nowUtc = LocalDateTime.now(ZoneId.of("UTC"));
        System.out.println("nowUtc= "+nowUtc);
        // Call the stored procedure for this interval
        System.out.println("START TIME= "+start);
        System.out.println("END TIME= "+end);
        System.out.println(" ZonedDateTime.now(); "+ ZonedDateTime.now());
        System.out.println(" LocalDateTime.now(); "+ LocalDateTime.now());
        
        ZonedDateTime nowUtcs = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime euroEndZoned = nowUtcs.withZoneSameInstant(ZoneId.of("CET"));
        
        LocalDateTime euroEnd = euroEndZoned.toLocalDateTime();
        LocalDateTime euroStart = euroEnd.minusMinutes(1);
        
       
        System.out.println(" euroEnd; "+ euroEnd);
        System.out.println(" euroStart; "+ euroStart);
    }
    // Scheduled task to run every 4 hours
    @Scheduled(cron = "0 0 */4 * * *") // Runs at 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
    public void schedule4HourIntervals() {
    	
        // Calculate the current interval's start and end times
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startTime = now.minusHours(4).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = now.withMinute(0).withSecond(0).withNano(0);
        String start = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // Call the stored procedure for this interval
        System.out.println("START TIME= "+start);
        System.out.println("END TIME= "+end);
        callInsertCryptosProcedure(start
            ,
            end
        );

        System.out.println("Scheduled Task Executed: " + startTime + " to " + endTime);
    }
}
