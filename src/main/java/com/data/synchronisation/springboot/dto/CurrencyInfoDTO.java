package com.data.synchronisation.springboot.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class CurrencyInfoDTO {
	@JsonProperty("id")
	private String id;
	@JsonProperty("symbol")
    private String symbol;
	@JsonProperty("name")
    private String name;
    @JsonProperty("market_cap")
    private String marketCap;
    @JsonProperty("fully_diluted_valuation")
    private String fullyDilutedMarketCap;
    @JsonProperty("total_volume")
    private String totalVolume;
    @JsonProperty("high_24h")
    private String high24h;
    @JsonProperty("low_24h")
    private String low24h;
    @JsonProperty("market_cap_change_24h")
    private String marketCapChange24h;
    @JsonProperty("market_cap_change_percentage_24h")
    private String marketCapChangePercentage24h;
    @JsonProperty("price_change_24h")
    private String priceChange24h;
    @JsonProperty("price_change_percentage_24h")
    private String priceChangePercentage24h;
    @JsonProperty("circulating_supply")
    private String circulatingSupply;
    @JsonProperty("total_supply")
    private String totalSupply;
    @JsonProperty("last_updated")
    private LocalDateTime referDate;
}