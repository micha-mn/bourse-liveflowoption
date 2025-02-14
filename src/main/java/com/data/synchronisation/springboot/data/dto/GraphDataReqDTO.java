package com.data.synchronisation.springboot.data.dto;

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
    private String cryptoCurrencyCode;
    private String period;
}