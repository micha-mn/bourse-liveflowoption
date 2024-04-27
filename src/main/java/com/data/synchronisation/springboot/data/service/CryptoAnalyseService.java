package com.data.synchronisation.springboot.data.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.data.synchronisation.springboot.data.dto.DataDTO;
import com.data.synchronisation.springboot.data.dto.PriceCryptoRespDTO;
import com.data.synchronisation.springboot.data.enums.TableNameEnum;
import com.data.synchronisation.springboot.domain.entity.Doge;
import com.data.synchronisation.springboot.domain.entity.Ena;
import com.data.synchronisation.springboot.domain.entity.EthFi;
import com.data.synchronisation.springboot.domain.entity.W;
import com.data.synchronisation.springboot.repositories.DogeRepository;
import com.data.synchronisation.springboot.repositories.EnaRepository;
import com.data.synchronisation.springboot.repositories.EthFiRepository;
import com.data.synchronisation.springboot.repositories.WRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Locale;


@Service
public class CryptoAnalyseService {
	
	@PersistenceContext
    private EntityManager entityManager;
	private EthFiRepository ethFiRepository;
	private EnaRepository enaRepository;
	private WRepository wRepository;
	private DogeRepository dogeRepository;
	
	public CryptoAnalyseService(EthFiRepository ethFiRepository,
			                    EnaRepository enaRepository,
			                    WRepository wRepository,
			                    DogeRepository dogeRepository) {
		this.ethFiRepository = ethFiRepository;
		this.enaRepository   = enaRepository;
		this.wRepository = wRepository;
		this.dogeRepository = dogeRepository;
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
	
	public boolean scheduledServiceDataSynchronization(PriceCryptoRespDTO data) {
		   buildEntityObjectAndInsert(data);
	       return true;
	}
	
	public void buildEntityObjectAndInsert(PriceCryptoRespDTO data) {
		if(data.getSymbol().equalsIgnoreCase("ETHFIUSDT")) {
			EthFi ethFi = EthFi.builder().referDate(LocalDateTime.now())
					.value(data.getPrice())
					.build();
			ethFiRepository.save(ethFi);
		}
		if(data.getSymbol().equalsIgnoreCase("ENAUSDT")) {
			Ena ena = Ena.builder().referDate(LocalDateTime.now())
					.value(data.getPrice())
					.build();
			enaRepository.save(ena);
		}
		if(data.getSymbol().equalsIgnoreCase("WUSDT")) {
			W w = W.builder().referDate(LocalDateTime.now())
					.value(data.getPrice())
					.build();
			wRepository.save(w);
		}
		if(data.getSymbol().equalsIgnoreCase("DOGEUSDT")) {
			Doge doge = Doge.builder().referDate(LocalDateTime.now())
					.value(data.getPrice())
					.build();
			dogeRepository.save(doge);
		}
	}
	
	
	
}
