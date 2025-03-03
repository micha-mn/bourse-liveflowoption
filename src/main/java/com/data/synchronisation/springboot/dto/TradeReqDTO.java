package com.data.synchronisation.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TradeReqDTO {
	private String currencyCode;
    private String datePoint;
    private String intervals; 
}