package com.data.synchronisation.springboot.data.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.data.synchronisation.springboot.data.dto.CurrencyDTO;
import com.data.synchronisation.springboot.data.dto.CurrencyInfoDTO;
import com.data.synchronisation.springboot.data.dto.DataDTO;
import com.data.synchronisation.springboot.data.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.data.dto.GraphFulllResponseDTO;
import com.data.synchronisation.springboot.data.dto.GraphGeneralResponseDTO;
import com.data.synchronisation.springboot.data.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.dto.Resistant;
import com.data.synchronisation.springboot.data.dto.SupResDTO;
import com.data.synchronisation.springboot.data.dto.Support;
import com.data.synchronisation.springboot.data.dto.SupportResistantPointsDTO;
import com.data.synchronisation.springboot.data.dto.TradeHistoryResDTO;
import com.data.synchronisation.springboot.data.dto.TradeInfoDTO;
import com.data.synchronisation.springboot.data.dto.TradeReqDTO;
import com.data.synchronisation.springboot.data.dto.TradeResponseDTO;
import com.data.synchronisation.springboot.data.enums.TableNameEnum;
import com.data.synchronisation.springboot.domain.entity.Bnb;
import com.data.synchronisation.springboot.domain.entity.Btc;
import com.data.synchronisation.springboot.domain.entity.Doge;
import com.data.synchronisation.springboot.domain.entity.Ena;
import com.data.synchronisation.springboot.domain.entity.EnaInfo;
import com.data.synchronisation.springboot.domain.entity.EnaTrackingTable;
import com.data.synchronisation.springboot.domain.entity.EnaTradeHistoryInfo;
import com.data.synchronisation.springboot.domain.entity.EnaTradeInfo;
import com.data.synchronisation.springboot.domain.entity.Eth;
import com.data.synchronisation.springboot.domain.entity.EthFITrackingTable;
import com.data.synchronisation.springboot.domain.entity.EthFITradeHistoryInfo;
import com.data.synchronisation.springboot.domain.entity.EthFi;
import com.data.synchronisation.springboot.domain.entity.Floki;
import com.data.synchronisation.springboot.domain.entity.Pepe;
import com.data.synchronisation.springboot.domain.entity.Saga;
import com.data.synchronisation.springboot.domain.entity.W;
import com.data.synchronisation.springboot.domain.entity.WInfo;
import com.data.synchronisation.springboot.domain.entity.WTradeInfo;
import com.data.synchronisation.springboot.repositories.BnbRepository;
import com.data.synchronisation.springboot.repositories.BtcRepository;
import com.data.synchronisation.springboot.repositories.DogeRepository;
import com.data.synchronisation.springboot.repositories.EnaInfoRepository;
import com.data.synchronisation.springboot.repositories.EnaRepository;
import com.data.synchronisation.springboot.repositories.EnaTrackingRepository;
import com.data.synchronisation.springboot.repositories.EnaTradeHistoryInfoRepository;
import com.data.synchronisation.springboot.repositories.EnaTradeInfoRepository;
import com.data.synchronisation.springboot.repositories.EthFITrackingRepository;
import com.data.synchronisation.springboot.repositories.EthFITradeHistoryInfoRepository;
import com.data.synchronisation.springboot.repositories.EthFiRepository;
import com.data.synchronisation.springboot.repositories.EthRepository;
import com.data.synchronisation.springboot.repositories.FlokiRepository;
import com.data.synchronisation.springboot.repositories.PepeRepository;
import com.data.synchronisation.springboot.repositories.SagaRepository;
import com.data.synchronisation.springboot.repositories.WInfoRepository;
import com.data.synchronisation.springboot.repositories.WRepository;
import com.data.synchronisation.springboot.repositories.WTradeInfoRepository;


@Service
public class CryptoAnalyseService {
	
	@PersistenceContext
    private EntityManager entityManager;
	private EthFiRepository ethFiRepository;
	private EnaRepository enaRepository;
	private WRepository wRepository;
	private DogeRepository dogeRepository;
	private SagaRepository sagaRepository;
	private BtcRepository btcRepository;
	private BnbRepository bnbRepository;
	private EthRepository ethRepository;
	private PepeRepository pepeRepository;
	private FlokiRepository flokiRepository;
	private EnaInfoRepository enaInfoRepository;
	private WInfoRepository wInfoRepository;
	private EnaTradeInfoRepository enaTradeInfoRepository;
	private WTradeInfoRepository  wTradeInfoRepository;
	private EnaTrackingRepository enaTrackingRepository;
	private EnaTradeHistoryInfoRepository enaTradeHistoryInfoRepository;
	private EthFITrackingRepository ethFITrackingRepository;
	private EthFITradeHistoryInfoRepository ethFITradeHistoryInfoRepository;
	
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public CryptoAnalyseService(EthFiRepository ethFiRepository,
			                    EnaRepository enaRepository,
			                    WRepository wRepository,
			                    DogeRepository dogeRepository,
			                    SagaRepository sagaRepository,
			                    BtcRepository btcRepository,
			                    BnbRepository bnbRepository,
			                    EthRepository ethRepository,
			                    PepeRepository pepeRepository,
			                    FlokiRepository flokiRepository,
			                    EnaInfoRepository enaInfoRepository,
			                    WInfoRepository wInfoRepository,
			                    EnaTradeInfoRepository enaTradeInfoRepository,
			                    WTradeInfoRepository  wTradeInfoRepository,
			                    EnaTrackingRepository enaTrackingRepository,
			                    EnaTradeHistoryInfoRepository enaTradeHistoryInfoRepository,
			                    EthFITrackingRepository ethFITrackingRepository,
			                    EthFITradeHistoryInfoRepository ethFITradeHistoryInfoRepository) {
		
		this.ethFiRepository                 = ethFiRepository;
		this.enaRepository                   = enaRepository;
		this.wRepository                     = wRepository;
		this.dogeRepository                  = dogeRepository;
		this.sagaRepository                  = sagaRepository;
		this.btcRepository                   = btcRepository;
		this.bnbRepository                   = bnbRepository;
		this.ethRepository                   = ethRepository;
		this.pepeRepository                  = pepeRepository;
		this.flokiRepository                 = flokiRepository;
		this.enaInfoRepository               = enaInfoRepository;
		this.wInfoRepository                 = wInfoRepository;
		this.enaTradeInfoRepository          = enaTradeInfoRepository;
		this.wTradeInfoRepository            = wTradeInfoRepository;
		this.enaTrackingRepository           = enaTrackingRepository;
		this.enaTradeHistoryInfoRepository   = enaTradeHistoryInfoRepository;
		this.ethFITrackingRepository         = ethFITrackingRepository;
		this.ethFITradeHistoryInfoRepository = ethFITradeHistoryInfoRepository;
	}
	
	
	/*
	 @Transactional
	    public boolean insertIntoTable(DataDTO dataDTO) {
	        String sequenceQuery = "select next_val from cr_" + dataDTO.getTableName() + "_sequence";
            
	        Query sequenceNativeQuery = entityManager.createNativeQuery(sequenceQuery);
	        BigInteger nextId = (BigInteger) sequenceNativeQuery.getSingleResult();

	        Long id = nextId.longValue();

	        String insertQuery = "insert into cr_" + dataDTO.getTableName() + " (id, refer_date, value) values (:id, :referDate, :value)";

	        Query nativeInsertQuery = entityManager.createNativeQuery(insertQuery);

	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy h:mm:ss a", Locale.ENGLISH);

	        //convert String to LocalDate
	        LocalDateTime localDate = LocalDateTime.parse(dataDTO.getReferDate(), formatter);
	        System.out.println(localDate);
	        
	        nativeInsertQuery.setParameter("id", id);
	        nativeInsertQuery.setParameter("referDate", localDate);
	        nativeInsertQuery.setParameter("value", dataDTO.getValue());

	        int rowsAffected = nativeInsertQuery.executeUpdate();
	        
	        updateNextVal(dataDTO.getTableName());
	        return true;
	    }
	    */
	    
	  @Transactional
	    public void updateNextVal(String tableName) {
	        String nativeQuery = "UPDATE cr_" + tableName + "_sequence SET next_val = next_val + 1";
	        entityManager.createNativeQuery(nativeQuery).executeUpdate();
	    }
	 
	  public List<Map<String, String>> getTableNameEnum()
	  {
		  TableNameEnum[] values = TableNameEnum.values();
	        List<Map<String, String>> resultList = new ArrayList<>();

	        for (TableNameEnum value : values) {
	            Map<String, String> map = new HashMap<>();
	            map.put("tableName", value.getTableName());
	            map.put("description", value.getDescription());
	            resultList.add(map);
	        }
	        return resultList;
	  }

	public List<DataDTO> getData(String tableName) {
	
		 String dataQuery = "select * from cr_" + tableName + " order by refer_date desc";
		    Query dataNativeQuery = entityManager.createNativeQuery(dataQuery);

		    List<Object[]> resultList = dataNativeQuery.getResultList();
		    List<DataDTO> dataDTOList = new ArrayList<>();

		    for (Object[] row : resultList) {
		        long id = ((Number) row[0]).longValue(); // Assuming the ID is a long
		        String value = (String) row[2];

		        DataDTO dataDTO =  DataDTO.builder().id(id)
		        									.referDate(String.valueOf( row[1]))
		        									.value(value)
		        									.tableName(tableName)
		        									.build();
		        dataDTOList.add(dataDTO);
		    }

		    return dataDTOList;
	}
	@Transactional
	public boolean updateData(DataDTO dataDTO) {
		
        String updateQuery = "update cr_" + dataDTO.getTableName() + " set  `value` =  :value WHERE `id` = :id";

        Query nativeUpdateQuery = entityManager.createNativeQuery(updateQuery);

        nativeUpdateQuery.setParameter("id", dataDTO.getId());
        nativeUpdateQuery.setParameter("value", dataDTO.getValue());

        int rowsAffected = nativeUpdateQuery.executeUpdate();
		return true;
	}
	@Transactional
	public boolean deleteData(String tablename, String id) {
		  String nativeQuery = "DELETE FROM cr_" + tablename + " where id= "+id;
	       entityManager.createNativeQuery(nativeQuery).executeUpdate();
	       return true;
	}
	
	
	
	public boolean saveLivePrices(PriceCryptoRespDTO[] dataLst) {
		   saveLivePricesAndCaclulateMinMAx(dataLst);
	       return true;
	}
	
	
	public void saveLivePricesAndCaclulateMinMAx(PriceCryptoRespDTO[] dataLst) {
		PriceCryptoRespDTO data = PriceCryptoRespDTO.builder().build();
		int checkTrackingCnt = 0;
		for(int i =0;i<dataLst.length; i++) {
			data = dataLst[i];
			if(data.getSymbol().equalsIgnoreCase("ETHFIUSDT")) {
				LocalDateTime ethFIInsertTime = LocalDateTime.now();
				Optional<EthFITrackingTable> ethFITrackingTableOpt ;
				EthFITrackingTable ethFITracking = EthFITrackingTable.builder().build();
				EthFi ethFi = EthFi.builder().referDate(LocalDateTime.now())
						.value(data.getPrice())
						.build();
				ethFiRepository.save(ethFi);
				ethFITrackingTableOpt = ethFITrackingRepository.findById(Long.valueOf("1"));
				ethFITracking = ethFITrackingTableOpt.get();
				checkTrackingCnt = Integer.parseInt(ethFITracking.getNotExecutedMinMaxPrice());
				if(checkTrackingCnt > 10) {
					// call procedure
					StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_calculate_max_min_graph");
			   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
			   		query.setParameter("cryptoCurrency","ETHFI" );
			   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
			   		query.setParameter("fromDate",ethFITracking.getLastDateMinMaxExecuted() );
			   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
			   		query.setParameter("toDate",ethFIInsertTime );
			   		query.registerStoredProcedureParameter("period", String.class, ParameterMode.IN);
			   		query.setParameter("period", "10" );
			   		query.execute();
				}else {
					ethFITracking.setNotExecutedMinMaxPrice(
							String.valueOf(Integer.parseInt(ethFITracking.getNotExecutedMinMaxPrice())+1)
							);
					// enaTracking.setLastDateMinMaxExecuted(LocalDateTime.now());
					ethFITrackingRepository.save(ethFITracking);
				}
			}else
			if(data.getSymbol().equalsIgnoreCase("ENAUSDT")) {
				LocalDateTime enaInsertTime = LocalDateTime.now();
				Optional<EnaTrackingTable> enaTrackingOpt ;
				EnaTrackingTable enaTracking = EnaTrackingTable.builder().build();
				enaInsertTime = LocalDateTime.now();
				Ena ena = Ena.builder().referDate(enaInsertTime)
						.value(data.getPrice())
						.build();
				enaRepository.save(ena);
				enaTrackingOpt = enaTrackingRepository.findById(Long.valueOf("1"));
				enaTracking = enaTrackingOpt.get();
				checkTrackingCnt = Integer.parseInt(enaTracking.getNotExecutedMinMaxPrice());
				if(checkTrackingCnt > 10) {
					// call procedure
					StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_calculate_max_min_graph");
			   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
			   		query.setParameter("cryptoCurrency","ENNA" );
			   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
			   		query.setParameter("fromDate",enaTracking.getLastDateMinMaxExecuted() );
			   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
			   		query.setParameter("toDate",enaInsertTime );
			   		query.registerStoredProcedureParameter("period", String.class, ParameterMode.IN);
			   		query.setParameter("period", "10" );
			   		query.execute();
				}else {
					enaTracking.setNotExecutedMinMaxPrice(
							String.valueOf(Integer.parseInt(enaTracking.getNotExecutedMinMaxPrice())+1)
							);
					// enaTracking.setLastDateMinMaxExecuted(LocalDateTime.now());
					enaTrackingRepository.save(enaTracking);
				}
				
			}else
			if(data.getSymbol().equalsIgnoreCase("WUSDT")) {
				W w = W.builder().referDate(LocalDateTime.now())
						.value(data.getPrice())
						.build();
				wRepository.save(w);
			}else
			if(data.getSymbol().equalsIgnoreCase("DOGEUSDT")) {
				Doge doge = Doge.builder().referDate(LocalDateTime.now())
						.value(data.getPrice())
						.build();
				dogeRepository.save(doge);
			}else
				if(data.getSymbol().equalsIgnoreCase("SAGAUSDT")) {
					Saga saga = Saga.builder().referDate(LocalDateTime.now())
							.value(data.getPrice())
							.build();
					sagaRepository.save(saga);
				}else
					if(data.getSymbol().equalsIgnoreCase("BTCUSDT")) {
						Btc btc = Btc.builder().referDate(LocalDateTime.now())
								.value(data.getPrice())
								.build();
						btcRepository.save(btc);
					}
					else
						if(data.getSymbol().equalsIgnoreCase("BNBUSDT")) {
							Bnb bnb = Bnb.builder().referDate(LocalDateTime.now())
									.value(data.getPrice())
									.build();
							bnbRepository.save(bnb);
						}else
							if(data.getSymbol().equalsIgnoreCase("ETHUSDT")) {
								Eth eth = Eth.builder().referDate(LocalDateTime.now())
										.value(data.getPrice())
										.build();
								ethRepository.save(eth);
							}else
								if(data.getSymbol().equalsIgnoreCase("PEPEUSDT")) {
									Pepe pepe = Pepe.builder().referDate(LocalDateTime.now())
											.value(data.getPrice())
											.build();
									pepeRepository.save(pepe);
								}else
									if(data.getSymbol().equalsIgnoreCase("FLOKIUSDT")) {
										Floki floki = Floki.builder().referDate(LocalDateTime.now())
												.value(data.getPrice())
												.build();
										flokiRepository.save(floki);
									}
		}
	}
	
	public void saveCurrencyInfo(CurrencyInfoDTO[] dataLst) {
		EnaInfo enaInfo = EnaInfo.builder().build();
		WInfo   wInfo = WInfo.builder().build();
		for(int i=0;i<dataLst.length;i++) {
			
		if(dataLst[i].getId().equalsIgnoreCase("ethena")) {
			enaInfo = buildEnaInfoEntity(dataLst[i]);
			enaInfoRepository.save(enaInfo);
		}else
			if(dataLst[i].getId().equalsIgnoreCase("wormhole")) {
			   wInfo = buildWInfoEntity(dataLst[i]);
			   wInfoRepository.save(wInfo);
		}
		
		
		}
	}
	
	
	public EnaInfo buildEnaInfoEntity(CurrencyInfoDTO data) {
		EnaInfo enaInfo = EnaInfo.builder().build();
		enaInfo = EnaInfo.builder()
				.id(data.getId())
				.circulatingSupply(data.getCirculatingSupply())
				.fullyDilutedMarketCap(data.getFullyDilutedMarketCap())
				.high24h(data.getHigh24h())
				.low24h(data.getLow24h())
				.marketCap(data.getMarketCap())
				.marketCapChange24h(data.getMarketCapChange24h())
				.marketCapChangePercentage24h(data.getMarketCapChangePercentage24h())
				.name(data.getName())
				.priceChange24h(data.getPriceChange24h())
				.priceChangePercentage24h(data.getPriceChangePercentage24h())
				.referDate(data.getReferDate())
				.symbol(data.getSymbol())
				.totalVolume(data.getTotalVolume())
				.build();
		return enaInfo;
	}
	
	public WInfo buildWInfoEntity(CurrencyInfoDTO data) {
		WInfo wInfo = WInfo.builder().build();
		wInfo = WInfo.builder()
				.id(data.getId())
				.circulatingSupply(data.getCirculatingSupply())
				.fullyDilutedMarketCap(data.getFullyDilutedMarketCap())
				.high24h(data.getHigh24h())
				.low24h(data.getLow24h())
				.marketCap(data.getMarketCap())
				.marketCapChange24h(data.getMarketCapChange24h())
				.marketCapChangePercentage24h(data.getMarketCapChangePercentage24h())
				.name(data.getName())
				.priceChange24h(data.getPriceChange24h())
				.priceChangePercentage24h(data.getPriceChangePercentage24h())
				.referDate(data.getReferDate())
				.symbol(data.getSymbol())
				.totalVolume(data.getTotalVolume())
				.build();
		return wInfo;
		
	}
	
	public void saveTradeInfo(TradeInfoDTO[] dataLst,String currrency) {
		
		
		if(currrency.equalsIgnoreCase("ENA")) {
			EnaTradeInfo enaTradeInfo = EnaTradeInfo.builder().build();
			List<EnaTradeInfo> enaTradeInfoLst = new ArrayList<EnaTradeInfo>();
			for(int i=0;i<dataLst.length;i++) {
				enaTradeInfo = buildEnaTradeInfoEntity(dataLst[i]);
				enaTradeInfoLst.add(enaTradeInfo);
			}
			enaTradeInfoRepository.saveAll(enaTradeInfoLst);
		}
	     else
			if(currrency.equalsIgnoreCase("W")) {
				WTradeInfo   wTradeInfo = WTradeInfo.builder().build();
				List<WTradeInfo> wTradeInfoLst = new ArrayList<WTradeInfo>();
				for(int i=0;i<dataLst.length;i++) {
					wTradeInfo = buildWTradeInfoEntity(dataLst[i]);
					wTradeInfoLst.add(wTradeInfo);
				}
				wTradeInfoRepository.saveAll(wTradeInfoLst);
		} 
	}
	
  public void saveHistoryTradeInfo(TradeInfoDTO[] dataLst,String currrency) {
		if(currrency.equalsIgnoreCase("ENNA")) {
			EnaTradeHistoryInfo enaTradeHistoryInfo = EnaTradeHistoryInfo.builder().build();
			List<EnaTradeHistoryInfo> enaTradeHistoryInfoLst = new ArrayList<EnaTradeHistoryInfo>();
			long test_timestamp ;
			LocalDateTime triggerTime ;
			        
			for(int i=0;i<dataLst.length;i++) {
				test_timestamp = Long.valueOf(dataLst[i].getTime());
				triggerTime =
				        LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp), 
				                                TimeZone.getDefault().toZoneId()); 
				enaTradeHistoryInfo = enaTradeHistoryInfo.builder()
						.id(dataLst[i].getId())
						.isBestMatch(dataLst[i].getIsBestMatch())
						.isBuyerMaker(dataLst[i].isBuyerMaker())
						.price(dataLst[i].getPrice())
						.qty(dataLst[i].getQty())
						.quoteQty(dataLst[i].getQuoteQty())
						.time(triggerTime)
						.build();
				enaTradeHistoryInfoLst.add(enaTradeHistoryInfo);
			}
			
			enaTradeHistoryInfoLst = enaTradeHistoryInfoLst.stream().distinct().collect(Collectors.toList());
			enaTradeHistoryInfoRepository.saveAll(enaTradeHistoryInfoLst);
		} else
			if(currrency.equalsIgnoreCase("ETHFI")) {
				EthFITradeHistoryInfo   ethFITradeHistoryInfo = EthFITradeHistoryInfo.builder().build();
				List<EthFITradeHistoryInfo> ethFITradeHistoryInfoLst = new ArrayList<EthFITradeHistoryInfo>();
				long test_timestamp ;
				LocalDateTime triggerTime ;
				
				for(int i=0;i<dataLst.length;i++) {
					test_timestamp = Long.valueOf(dataLst[i].getTime());
					triggerTime =
					        LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp), 
					                                TimeZone.getDefault().toZoneId()); 
					ethFITradeHistoryInfo = EthFITradeHistoryInfo.builder()
							.id(dataLst[i].getId())
							.isBestMatch(dataLst[i].getIsBestMatch())
							.isBuyerMaker(dataLst[i].isBuyerMaker())
							.price(dataLst[i].getPrice())
							.qty(dataLst[i].getQty())
							.quoteQty(dataLst[i].getQuoteQty())
							.time(triggerTime)
							.build();
					ethFITradeHistoryInfoLst.add(ethFITradeHistoryInfo);
				}
				
				ethFITradeHistoryInfoLst = ethFITradeHistoryInfoLst.stream().distinct().collect(Collectors.toList());
				ethFITradeHistoryInfoRepository.saveAll(ethFITradeHistoryInfoLst);
		} 
	     else
			if(currrency.equalsIgnoreCase("W")) {
				WTradeInfo   wTradeInfo = WTradeInfo.builder().build();
				List<WTradeInfo> wTradeInfoLst = new ArrayList<WTradeInfo>();
				for(int i=0;i<dataLst.length;i++) {
					wTradeInfo = buildWTradeInfoEntity(dataLst[i]);
					wTradeInfoLst.add(wTradeInfo);
				}
				wTradeInfoRepository.saveAll(wTradeInfoLst);
		} 
	}
	
  
  
	public EnaTradeInfo buildEnaTradeInfoEntity(TradeInfoDTO data) {
		long test_timestamp = Long.valueOf(data.getTime());
		LocalDateTime triggerTime =
		        LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp), 
		                                TimeZone.getDefault().toZoneId()); 
		
		EnaTradeInfo enaTradeInfo = EnaTradeInfo.builder().build();
		enaTradeInfo = EnaTradeInfo.builder()
				.id(data.getId())
				.isBestMatch(data.getIsBestMatch())
				.isBuyerMaker(data.isBuyerMaker())
				.price(data.getPrice())
				.qty(data.getQty())
				.quoteQty(data.getQuoteQty())
				.time(triggerTime)
				.build();
		return enaTradeInfo;
	}
	
	public EnaTradeInfo buildEthFITradeInfoEntity(TradeInfoDTO data) {
		long test_timestamp = Long.valueOf(data.getTime());
		LocalDateTime triggerTime =
		        LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp), 
		                                TimeZone.getDefault().toZoneId()); 
		
		EnaTradeInfo enaTradeInfo = EnaTradeInfo.builder().build();
		enaTradeInfo = EnaTradeInfo.builder()
				.id(data.getId())
				.isBestMatch(data.getIsBestMatch())
				.isBuyerMaker(data.isBuyerMaker())
				.price(data.getPrice())
				.qty(data.getQty())
				.quoteQty(data.getQuoteQty())
				.time(triggerTime)
				.build();
		return enaTradeInfo;
	}
	
	public WTradeInfo buildWTradeInfoEntity(TradeInfoDTO data) {
		
		long test_timestamp = Long.valueOf(data.getTime());
		LocalDateTime triggerTime =
		        LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp), 
		                                TimeZone.getDefault().toZoneId()); 
		
		WTradeInfo wTradeInfo = WTradeInfo.builder().build();
		wTradeInfo = WTradeInfo.builder()
				.id(data.getId())
				.isBestMatch(data.getIsBestMatch())
				.isBuyerMaker(data.isBuyerMaker())
				.price(data.getPrice())
				.qty(data.getQty())
				.quoteQty(data.getQuoteQty())
				.time(triggerTime)
				.build();
		return wTradeInfo;
	}
	
	public GraphFulllResponseDTO getGraphData(@RequestBody GraphDataReqDTO req) {
		
		LocalDateTime fromDate = LocalDateTime.parse(req.getFromDate(), formatter);
		LocalDateTime toDate = LocalDateTime.parse(req.getToDate(), formatter);
		GraphGeneralResponseDTO respMax = null;
		GraphGeneralResponseDTO respMin = null;
		
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_data_for_graph",GraphResponseDTO.class);
   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
   		query.setParameter("cryptoCurrency",req.getCryptoCurrencyCode() );
   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("fromDate",fromDate );
   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("toDate",toDate );
   		query.registerStoredProcedureParameter("period", Integer.class, ParameterMode.IN);
   		query.setParameter("period",0 );
   		query.registerStoredProcedureParameter("currencytype", String.class, ParameterMode.IN);
   		// query.setParameter("currencytype",req.getDataType());
   		query.setParameter("currencytype",req.getDataType());
   		List<GraphResponseDTO> graphNormalResponseDTOlst = (List<GraphResponseDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		GraphGeneralResponseDTO respNormal = GraphGeneralResponseDTO.builder()
				.data(graphNormalResponseDTOlst)
				.name("NORMAL")
				.build();
		
		
		/*
		query = this.entityManager.createStoredProcedureQuery("cr_data_for_graph",GraphResponseDTO.class);
   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
   		query.setParameter("cryptoCurrency",req.getCryptoCurrencyCode() );
   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("fromDate",fromDate );
   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("toDate",toDate );
   		query.registerStoredProcedureParameter("period", Integer.class, ParameterMode.IN);
   		query.setParameter("period",0 );
   		query.registerStoredProcedureParameter("currencytype", String.class, ParameterMode.IN);
   		// query.setParameter("currencytype",req.getDataType());
   		query.setParameter("currencytype","MAX");
   		List<GraphResponseDTO> graphMaxResponseDTOlst = (List<GraphResponseDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		respMax = GraphGeneralResponseDTO.builder()
				.data(graphMaxResponseDTOlst)
				//.name(req.getDataType())
				.name("MAX")
				.build();
		
		
		
		
		query = this.entityManager.createStoredProcedureQuery("cr_data_for_graph",GraphResponseDTO.class);
   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
   		query.setParameter("cryptoCurrency",req.getCryptoCurrencyCode() );
   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("fromDate",fromDate );
   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("toDate",toDate );
   		query.registerStoredProcedureParameter("period", Integer.class, ParameterMode.IN);
   		query.setParameter("period",0 );
   		query.registerStoredProcedureParameter("currencytype", String.class, ParameterMode.IN);
   		// query.setParameter("currencytype",req.getDataType());
   		query.setParameter("currencytype","MIN");
   		List<GraphResponseDTO> graphRespMinResponseDTOlst = (List<GraphResponseDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		respMin = GraphGeneralResponseDTO.builder()
				.data(graphRespMinResponseDTOlst)
				//.name(req.getDataType())
				.name("MIN")
				.build();	
		*/
		
		GraphFulllResponseDTO resp = GraphFulllResponseDTO.builder()
				.dataMax(respMax)
				.dataMin(respMin)
				.dataNormal(respNormal)
				.build();
	
	   return resp; 
	}

	public GraphFulllResponseDTO getCandleGraphData(@RequestBody GraphDataReqDTO req) {
		
		LocalDateTime fromDate = LocalDateTime.parse(req.getFromDate(), formatter);
		LocalDateTime toDate = LocalDateTime.parse(req.getToDate(), formatter);
		String tableName=null;	
		if(req.getCryptoCurrencyCode().equalsIgnoreCase("BTC"))
			tableName="cr_btc_high_low";
		
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_dynamic_calculation_candlestick_graph",GraphResponseDTO.class);
		
   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("fromDate",fromDate );
   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("toDate",toDate );
   		query.registerStoredProcedureParameter("tableName", String.class, ParameterMode.IN);
   		query.setParameter("tableName", tableName);
   		query.registerStoredProcedureParameter("period", String.class, ParameterMode.IN);
   		query.setParameter("period",req.getPeriod() );
   		
   		List<GraphResponseDTO> graphNormalResponseDTOlst = (List<GraphResponseDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		GraphGeneralResponseDTO respNormal = GraphGeneralResponseDTO.builder()
				.data(graphNormalResponseDTOlst)
				.name("NORMAL")
				.build();
		

		query = this.entityManager.createStoredProcedureQuery("cr_dynamic_calculation_volume_graph",GraphResponseDTO.class);
		
   		query.registerStoredProcedureParameter("fromDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("fromDate",fromDate );
   		query.registerStoredProcedureParameter("toDate", LocalDateTime.class, ParameterMode.IN);
   		query.setParameter("toDate",toDate );
   		query.registerStoredProcedureParameter("tableName", String.class, ParameterMode.IN);
   		query.setParameter("tableName", tableName);
   		query.registerStoredProcedureParameter("period", String.class, ParameterMode.IN);
   		query.setParameter("period",req.getPeriod() );
   		
   		List<GraphResponseDTO> graphVolumeResponseDTOlst = (List<GraphResponseDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		GraphGeneralResponseDTO respVolume= GraphGeneralResponseDTO.builder()
				.data(graphVolumeResponseDTOlst)
				.name("VOLUME")
				.build();
		
		GraphFulllResponseDTO resp = GraphFulllResponseDTO.builder()
				.dataCandle(respNormal)
				.dataVolume(respVolume)
				.build();
	
	   return resp; 
	}
	
	public SupportResistantPointsDTO getSupportResistantForGraph(@RequestBody GraphDataReqDTO req) {
		
		
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_support_resistant_for_graph",SupResDTO.class);
   		query.registerStoredProcedureParameter("cryptoCurrency", String.class, ParameterMode.IN);
   		query.setParameter("cryptoCurrency",req.getCryptoCurrencyCode() );
   		List<SupResDTO> supResDTOLst = (List<SupResDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		SupportResistantPointsDTO suppResPts = SupportResistantPointsDTO.builder().build();
		Support sup = Support.builder().build();
		Resistant res = Resistant.builder().build();
		int i = 1;
		for(SupResDTO each : supResDTOLst) {
			if(i==1) {
				sup.setSupport1(each.getSupport());
				res.setResistant1(each.getResistant());
			}
			if(i==2) {
				sup.setSupport2(each.getSupport());
				res.setResistant2(each.getResistant());
			}
			if(i==3) {
				sup.setSupport3(each.getSupport());
				res.setResistant3(each.getResistant());
			}
			i++;
		}
		suppResPts.setSupport(sup);
		suppResPts.setResistant(res);
		return suppResPts;
	}
	
    public TradeResponseDTO getTradeHistory( TradeReqDTO req) {
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("cr_analyse_trade_infor_history_for_graph",TradeHistoryResDTO.class);
   		query.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
   		query.setParameter("currencyCode",req.getCurrencyCode() );
   		
   		query.registerStoredProcedureParameter("datePoint", String.class, ParameterMode.IN);
   		query.setParameter("datePoint",req.getDatePoint() );
   		
   		query.registerStoredProcedureParameter("intervals", String.class, ParameterMode.IN);
   		query.setParameter("intervals",req.getIntervals() );
   		
   		List<TradeHistoryResDTO> tradeHistoryResDTOLst = (List<TradeHistoryResDTO>) query.getResultList();
   		entityManager.clear();
		entityManager.close();
		TradeHistoryResDTO tradeHistoryResDTO = TradeHistoryResDTO.builder().build();
		TradeResponseDTO tradeResponseDTO = TradeResponseDTO.builder().build();
		List respArr= new ArrayList<>();
		
		if(tradeHistoryResDTOLst.size()>0) {
			tradeHistoryResDTO = tradeHistoryResDTOLst.get(0);
	    
		    respArr.add(tradeHistoryResDTO.getBuy15Min());
		    respArr.add(tradeHistoryResDTO.getSell15Min());
		    tradeResponseDTO.setHistory15Min(respArr);
		    
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy30Min());
		    respArr.add(tradeHistoryResDTO.getSell30Min());
		    tradeResponseDTO.setHistory30Min(respArr);
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy45Min());
		    respArr.add(tradeHistoryResDTO.getSell45Min());
		    tradeResponseDTO.setHistory45Min(respArr);
		    
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy1Hour());
		    respArr.add(tradeHistoryResDTO.getSell1Hour());
		    tradeResponseDTO.setHistory1Hour(respArr);
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy2Hour());
		    respArr.add(tradeHistoryResDTO.getSell2Hour());
		    tradeResponseDTO.setHistory2Hour(respArr);
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy4Hour());
		    respArr.add(tradeHistoryResDTO.getSell4Hour());
		    tradeResponseDTO.setHistory4Hour(respArr);
		    
		    respArr= new ArrayList<>();
		    respArr.add(tradeHistoryResDTO.getBuy1Day());
		    respArr.add(tradeHistoryResDTO.getSell1Day());
		    tradeResponseDTO.setHistory1Day(respArr);
		}
	    
		return tradeResponseDTO;
	}
    
    public List<CurrencyDTO> getCurrencyList(){
    	
    	List<CurrencyDTO> lst = new ArrayList<CurrencyDTO>();
    	CurrencyDTO dto = CurrencyDTO.builder().id(1)
    			.symbol("ENNA")
    			.name("ENNA")
    			.build();
    	lst.add(dto);
    	
    	dto = CurrencyDTO.builder().id(2)
    			.symbol("ETHFI")
    			.name("ETHFI")
    			.build();
    	lst.add(dto);
    	return lst;
    }
	
}
