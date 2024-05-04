package com.data.synchronisation.springboot.data.service;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.data.synchronisation.springboot.data.dto.CurrencyInfoDTO;
import com.data.synchronisation.springboot.data.dto.DataDTO;
import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.dto.TradeInfoDTO;
import com.data.synchronisation.springboot.data.enums.TableNameEnum;
import com.data.synchronisation.springboot.domain.entity.Bnb;
import com.data.synchronisation.springboot.domain.entity.Btc;
import com.data.synchronisation.springboot.domain.entity.Doge;
import com.data.synchronisation.springboot.domain.entity.Ena;
import com.data.synchronisation.springboot.domain.entity.EnaInfo;
import com.data.synchronisation.springboot.domain.entity.EnaTradeInfo;
import com.data.synchronisation.springboot.domain.entity.Eth;
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
import com.data.synchronisation.springboot.repositories.EnaTradeInfoRepository;
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
			                    WTradeInfoRepository  wTradeInfoRepository) {
		
		this.ethFiRepository            = ethFiRepository;
		this.enaRepository              = enaRepository;
		this.wRepository                = wRepository;
		this.dogeRepository             = dogeRepository;
		this.sagaRepository             = sagaRepository;
		this.btcRepository              = btcRepository;
		this.bnbRepository              = bnbRepository;
		this.ethRepository              = ethRepository;
		this.pepeRepository             = pepeRepository;
		this.flokiRepository            = flokiRepository;
		this.enaInfoRepository          = enaInfoRepository;
		this.wInfoRepository            = wInfoRepository;
		this.enaTradeInfoRepository     = enaTradeInfoRepository;
		this.wTradeInfoRepository       = wTradeInfoRepository;
	}
	
	
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
	
	public boolean scheduledServiceDataSynchronization(PriceCryptoRespDTO[] dataLst) {
		   buildEntityObjectAndInsert(dataLst);
	       return true;
	}
	
	public void buildEntityObjectAndInsert(PriceCryptoRespDTO[] dataLst) {
		PriceCryptoRespDTO data = PriceCryptoRespDTO.builder().build();
		for(int i =0;i<dataLst.length; i++) {
			data = dataLst[i];
			if(data.getSymbol().equalsIgnoreCase("ETHFIUSDT")) {
				EthFi ethFi = EthFi.builder().referDate(LocalDateTime.now())
						.value(data.getPrice())
						.build();
				ethFiRepository.save(ethFi);
			}else
			if(data.getSymbol().equalsIgnoreCase("ENAUSDT")) {
				Ena ena = Ena.builder().referDate(LocalDateTime.now())
						.value(data.getPrice())
						.build();
				enaRepository.save(ena);
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
	
}
