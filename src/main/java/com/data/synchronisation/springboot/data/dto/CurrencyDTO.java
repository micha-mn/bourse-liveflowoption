package com.data.synchronisation.springboot.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class CurrencyDTO {
	@JsonProperty("id")
	private int id;
	@JsonProperty("symbol")
    private String symbol;
	@JsonProperty("name")
    private String name;
}