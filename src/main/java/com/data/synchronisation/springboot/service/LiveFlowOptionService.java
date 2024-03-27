package com.data.synchronisation.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.springframework.stereotype.Service;

import com.data.synchronisation.springboot.domain.LiveOptionFlow;
import com.data.synchronisation.springboot.dto.LiveOptionFlowDTO;
import com.data.synchronisation.springboot.repositories.LiveOptionFlowRepository;

@Service
public class LiveFlowOptionService {
	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 @Value("${procedure.debugging.mode}")
		private boolean debuggingMode;
    
	 @Autowired
	 LiveOptionFlowRepository liveOptionFlowRepository;
	 
	 public LiveOptionFlow saveLiveOptionFlowData(List<LiveOptionFlowDTO> liveOptionFlowDTO) {
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		    LocalDate localDate = LocalDate.parse(String.valueOf(liveOptionFlowDTO.get(0).getFlowDate()),formatter);
		    Date date = convertLocalDateToDate(localDate);
		    LiveOptionFlow liveOptionFlow =LiveOptionFlow.builder().flow(liveOptionFlowDTO.get(0).getFlow()).flowDate(date).product(liveOptionFlowDTO.get(0).getProduct()).build();
			return liveOptionFlowRepository.save(liveOptionFlow);
		}
	 public List<LiveOptionFlow> getLiveOptionFlowDataByDate(String Date) {
		   String pattern = "yyyy-MM-dd";
		   Date convertedDate = null;
		   try {
			    convertedDate = convertStringToDate(Date, pattern);
		     } catch (ParseException e) {
	            e.printStackTrace();
	        }
		   return liveOptionFlowRepository.findLiveOptionFlowByFlowDate(Date);
		}
	 public void deleteLiveOptionFlow(long id)
		{  
		 liveOptionFlowRepository.deleteById(id);
		}
	 public List<LiveOptionFlow> getLiveOptionFlowDataByValue(String value) {
		   return liveOptionFlowRepository.findAllNewsByDescription(value);
		} 
	 public List<LiveOptionFlow> getLiveOptionFlowDataByValueAndProduct(String value,String product) {
		   return liveOptionFlowRepository.findAllNewsByDescriptionAndProduct(value,product);
		} 
	  public static Date convertLocalDateToDate(LocalDate localDate) {
	        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    }
	  public static Date convertStringToDate(String dateString, String pattern) throws ParseException {
	        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	        return sdf.parse(dateString);
	    }
	public LiveOptionFlow updateFlowById(LiveOptionFlow liveOptionFlow) {
		 return liveOptionFlowRepository.save(liveOptionFlow);
	}
}
