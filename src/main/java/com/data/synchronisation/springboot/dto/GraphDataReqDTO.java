package com.data.synchronisation.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class GraphDataReqDTO {
	private Long id;
    private String fromDate;
    private String toDate;
    private String dataType;  // normal max min 
    private String cryptoCurrencyCode; //btc
    private String period;   // 1 min 5 min ...
    private String hmd;   // hour or minute or day..
    private String action;   // sell buy
    private String criteria;
    private int minutes;   // used for order book
    private int page;
    private int size;
    private int limit;
}