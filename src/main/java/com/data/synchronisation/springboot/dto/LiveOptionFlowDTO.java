package com.data.synchronisation.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class LiveOptionFlowDTO {
	private String product;
    private String flowDate;
    private String flow;
}
