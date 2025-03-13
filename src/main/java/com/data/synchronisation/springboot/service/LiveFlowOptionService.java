package com.data.synchronisation.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
		    LocalDate localDate = LocalDate.parse(String.valueOf(liveOptionFlowDTO.get(0).getFlowDate()), formatter);

		    // Step 1: Create midnight UTC
		    ZonedDateTime midnightUtc = localDate.atStartOfDay(ZoneOffset.UTC);

		    // Step 2: Shift to local JVM timezone, which will be re-shifted back by Hibernate
		    ZonedDateTime adjustedToLocalPlus6 = midnightUtc.withZoneSameInstant(ZoneId.systemDefault())
                    .plusHours(6);
			
			// Step 3: Create a Date that, when Hibernate writes it from local time, will become the adjusted time
			Date date = Date.from(adjustedToLocalPlus6.toInstant());

		    LiveOptionFlow liveOptionFlow = LiveOptionFlow.builder()
		            .flow(liveOptionFlowDTO.get(0).getFlow())
		            .flowDate(date)
		            .product(liveOptionFlowDTO.get(0).getProduct())
		            .build();

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

		    // Create calendar instance and set time to the converted date
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(convertedDate);
		    
		    // Set to start of day: 00:00:00
		    cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
		    Date startDate = cal.getTime();
		    
		    // Set to end of day: 23:59:59 (or 23:59:59.999 if you want the very end)
		    cal.set(Calendar.HOUR_OF_DAY, 23);
		    cal.set(Calendar.MINUTE, 59);
		    cal.set(Calendar.SECOND, 59);
		    cal.set(Calendar.MILLISECOND, 999);
		    Date endDate = cal.getTime();
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String startDateStr = sdf.format(startDate);
		    String endDateStr = sdf.format(endDate);
		    
		   return liveOptionFlowRepository.findLiveOptionFlowByFlowDate(startDateStr,endDateStr);
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
