package com.data.synchronisation.springboot.domain.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "CR_W_INFO")
public class WInfo {
	@Id
	 @GeneratedValue(generator = "CR_W_INFO_SEQ")
	 @GenericGenerator(
	      name = "CR_W_INFO_SEQ",
	      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	      parameters = {
	        @Parameter(name = "sequence_name", value = "CR_W_INFO_SEQ"),
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	        }
	    )
	private Long id1;
	@Column(name = "ID")
	private String id;
	@Column(name = "SYMBOL")
    private String symbol;
	@Column(name = "NAME")
    private String name;
    @Column(name = "MARKET_CAP")
    private String marketCap;
    @Column(name = "FULLY_DILUTED_VALUATION")
    private String fullyDilutedMarketCap;
    @Column(name = "TOTAL_VOLUME")
    private String totalVolume;
    @Column(name = "HIGH_24H")
    private String high24h;
    @Column(name = "LOW_24H")
    private String low24h;
    @Column(name = "MARKET_CAP_CHANGE_24H")
    private String marketCapChange24h;
    @Column(name = "MARKET_CAP_CHANGE_PERCENTAGE_24H")
    private String marketCapChangePercentage24h;
    @Column(name = "PRICE_CHANGE_24H")
    private String priceChange24h;
    @Column(name = "PRICE_CHANGE_PERCENTAGE_24H")
    private String priceChangePercentage24h;
    @Column(name = "CIRCULATING_SUPPLY")
    private String circulatingSupply;
    @Column(name = "TOTAL_SUPPLY")
    private String totalSupply;
    @Column(name = "REFER_DATE")
    private LocalDateTime referDate;
}
