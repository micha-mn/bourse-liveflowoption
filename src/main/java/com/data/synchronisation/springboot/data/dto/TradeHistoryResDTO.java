package com.data.synchronisation.springboot.data.dto;

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
	private String buy;
	private String sell;
}
