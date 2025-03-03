package com.data.synchronisation.springboot.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TradeHistoryResDTO {
	
	@Id
	private String id;
	
	private String buy15Min;
	private String sell15Min;
	
	private String buy30Min;
	private String sell30Min;
	
	private String buy45Min;
	private String sell45Min;
	
	private String buy1Hour;
	private String sell1Hour;
	
	private String buy2Hour;
	private String sell2Hour;
	
	private String buy4Hour;
	private String sell4Hour;
	
	private String buy1Day;
	private String sell1Day;
}
